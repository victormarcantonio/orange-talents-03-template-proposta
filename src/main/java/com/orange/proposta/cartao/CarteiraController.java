package com.orange.proposta.cartao;

import com.orange.proposta.compartilhado.ApiErroException;
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
public class CarteiraController {

    private CartaoRepository cartaoRepository;
    private CarteiraRepository carteiraRepository;
    private CartaoClient cartaoClient;

    public CarteiraController(CartaoRepository cartaoRepository, CarteiraRepository carteiraRepository, CartaoClient cartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraRepository = carteiraRepository;
        this.cartaoClient = cartaoClient;
    }

    @PostMapping("/carteiras/{idCartao}")
    public ResponseEntity<?> associaCarteira(@PathVariable ("idCartao") @NotBlank String idCartao, @RequestBody @Valid CarteiraRequest request, UriComponentsBuilder uriBuilder){
        Optional<Cartao> possivelCartao = cartaoRepository.findById(Long.parseLong(idCartao.replace("-","")));
        if(possivelCartao.isPresent()){
            Cartao cartao = possivelCartao.get();
            if(!cartao.possuiCarteira(request.getCarteira())) {
                try{
                    cartaoClient.associaCarteira(idCartao, request);
                }catch (FeignException e){
                    throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Carteira não registrada");
                }
                Carteira carteira = request.converter(cartao);
                carteiraRepository.save(carteira);
                URI uri = uriBuilder.path("/cartoes/carteiras/{id}").buildAndExpand(carteira.getId()).toUri();
                return ResponseEntity.created(uri).build();
            }
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já associado a carteira " + request.getCarteira());
        }
        return ResponseEntity.notFound().build();
    }
}
