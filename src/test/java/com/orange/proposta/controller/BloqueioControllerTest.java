/*
package com.orange.proposta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.proposta.cartao.BloqueioRequest;
import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.CartaoRepository;
import com.orange.proposta.criaProposta.Proposta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ExtendWith(MockitoExtension.class)
public class BloqueioControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Mock
    CartaoRepository cartaoRepository;


    @BeforeEach
    void init(){

    }



    @DisplayName("Deve bloquear o cartão e retornar status 200")
    @Test
    void teste01() throws Exception{
        Proposta proposta = new Proposta("997.285.210-57","vitin@email.com","vitin","rua fidalga",new BigDecimal("2500.0"));
        Cartao cartao = new Cartao(2508532787248333L,LocalDateTime.of(2021, Month.APRIL,24,14,35,30,1),proposta);
        Mockito.when(cartaoRepository.findById(2508532787248333L)).thenReturn(Optional.of(cartao));
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/bloqueio/2508-5327-8724-8333")
        .content(json(new BloqueioRequest()))
        .header("User-Agent","Postman")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    public String json(BloqueioRequest bloqueioRequest) throws JsonProcessingException {
        bloqueioRequest.setSistemaResponsavel("proposta");
        return mapper.writeValueAsString(bloqueioRequest);
    }
}
*/
