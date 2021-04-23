package com.orange.proposta.cartao;

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

    CartaoRepository cartaoRepository;
    BloqueioRepository bloqueioRepository;
    CartaoClient cartaoClient;


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
               Bloqueio bloqueio = request.converter(httpRequest.getRemoteAddr(),userAgent,cartaoRepository,cartao);
               try {
                   cartaoClient.bloqueiaCartao(idCartao, request);
               }catch(FeignException e){
                   throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já bloqueado");
               }
               bloqueioRepository.save(bloqueio);
               URI uri= uriBuilder.path("/cartoes/bloqueio/{id}").buildAndExpand(cartao.getId()).toUri();
               return ResponseEntity.created(uri).build();
       }
       return ResponseEntity.notFound().build();
    }
}
