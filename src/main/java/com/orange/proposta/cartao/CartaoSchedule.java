package com.orange.proposta.cartao;

import com.orange.proposta.compartilhado.FeignErroException;
import com.orange.proposta.criaProposta.*;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartaoSchedule {

    private PropostaRepository propostaRepository;
    private CartaoRepository cartaoRepository;
    private CartaoClient cartaoClient;

    public CartaoSchedule(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(fixedDelay = 5000)
    public void verificaSeCartaoFoiGerado(){
        List<Proposta> propostas  = propostaRepository.findByCartaoIsNullAndStatusIs(Status.ELEGIVEL);
        propostas.forEach(this::associaCartao);
    }


    public void associaCartao(Proposta proposta) {
        try {
            Optional<CartaoResponse> cartaoResponse = cartaoClient.retornaCartao(proposta.getId().toString());
            if (cartaoResponse.isPresent()) {
                Cartao cartao = proposta.criaCartao(cartaoResponse.get());
                cartaoRepository.save(cartao);
            }
        } catch (FeignException e) {
            throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão não gerado, aguardar próximo processamento");
        }
    }
}
