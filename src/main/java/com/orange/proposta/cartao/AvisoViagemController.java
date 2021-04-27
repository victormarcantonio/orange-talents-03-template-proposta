package com.orange.proposta.cartao;

import com.orange.proposta.compartilhado.FeignErroException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class AvisoViagemController {


    private CartaoRepository cartaoRepository;
    private AvisoViagemRepository avisoViagemRepository;
    private CartaoClient cartaoClient;

    public AvisoViagemController(CartaoRepository cartaoRepository, AvisoViagemRepository avisoViagemRepository, CartaoClient cartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.cartaoClient = cartaoClient;
    }

    @PostMapping("/aviso-viagem/{idCartao}")
    public ResponseEntity<?> cadastrarAvisoViagem(@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest httpServletRequest,
                                                  @PathVariable("idCartao") @NotBlank String idCartao,
                                                  @RequestBody @Valid AvisoViagemRequest request, UriComponentsBuilder uriBuilder){
        Optional<Cartao> possivelCartao = cartaoRepository.findById(Long.parseLong(idCartao.replace("-","")));
        if(possivelCartao.isPresent()){
            try{
                cartaoClient.avisaViagem(idCartao, request);
            }catch (FeignException e){
                throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Aviso viagem não cadastrado");
            }
            Cartao cartao = possivelCartao.get();
            AvisoViagem avisoViagem = request.converter(userAgent,httpServletRequest.getRemoteAddr(),cartao);
            avisoViagemRepository.save(avisoViagem);
            URI uri = uriBuilder.path("/cartoes/aviso-viagem/{id}").buildAndExpand(avisoViagem.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }

        return ResponseEntity.notFound().build();
    }
}
