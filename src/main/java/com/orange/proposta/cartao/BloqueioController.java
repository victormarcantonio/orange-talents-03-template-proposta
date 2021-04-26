package com.orange.proposta.cartao;

import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.compartilhado.FeignErroException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class BloqueioController {

    private CartaoRepository cartaoRepository;
    private BloqueioRepository bloqueioRepository;
    private CartaoClient cartaoClient;


    public BloqueioController(CartaoRepository cartaoRepository, BloqueioRepository bloqueioRepository, CartaoClient cartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.cartaoClient = cartaoClient;
    }

    @PostMapping("/bloqueio/{idCartao}")
    public ResponseEntity<?> bloqueia(@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest httpRequest, @PathVariable ("idCartao") String idCartao, @RequestBody @Valid BloqueioRequest request, UriComponentsBuilder uriBuilder){
       Optional<Cartao> possivelCartao = cartaoRepository.findById(Long.parseLong(idCartao.replace("-", "")));
       if(possivelCartao.isPresent()){
           Cartao cartao = possivelCartao.get();
           Optional<Bloqueio> possivelBloqueio = bloqueioRepository.findByCartao(cartao);
           if(possivelBloqueio.isEmpty()) {
               Bloqueio bloqueio = request.converter(httpRequest.getRemoteAddr(), userAgent, cartaoRepository, cartao);
               try {
                   cartaoClient.bloqueiaCartao(idCartao, request);
               } catch (FeignException e) {
                   throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Ocorreu alguma falha no sistema de bloquear cartão");
               }
               cartao.bloqueia();
               cartaoRepository.save(cartao);
               bloqueioRepository.save(bloqueio);
               URI uri = uriBuilder.path("/cartoes/bloqueio/{id}").buildAndExpand(cartao.getId()).toUri();
               return ResponseEntity.created(uri).build();
           }
           throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta já bloqueada");
       }
       return ResponseEntity.notFound().build();
    }
}
