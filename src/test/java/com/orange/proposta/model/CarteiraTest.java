package com.orange.proposta.model;

import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.Carteira;
import com.orange.proposta.cartao.TipoCarteira;
import com.orange.proposta.criaProposta.Proposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

public class CarteiraTest {

    Proposta proposta = new Proposta("997.285.210-57","vitin@email.com","vitin","rua fidalga",new BigDecimal("2500.0"));
    Cartao cartao = new Cartao(2508532787248333L, LocalDateTime.of(2021, Month.APRIL,24,14,35,30,1),proposta);

    @DisplayName("Deve retornar false caso o cartão não possua carteira PAYPAL vinculada")
    @Test
    void teste01(){
        Assertions.assertFalse(cartao.possuiCarteira("PAYPAL"));
    }

    @DisplayName("Deve retornar true caso o cartão possua a carteira PAYPAL vinculada")
    @Test
    void teste02(){
        Cartao cartao2 = new Cartao(2508532787248333L, LocalDateTime.of(2021, Month.APRIL,24,14,35,30,1),proposta);
        Set<Carteira>carteiras = Set.of(
                new Carteira("vitin@email.com", TipoCarteira.PAYPAL, cartao2)
        );
        ReflectionTestUtils.setField(cartao,"carteiras", carteiras);
        Assertions.assertTrue(cartao.possuiCarteira("PAYPAL"));
    }
}
