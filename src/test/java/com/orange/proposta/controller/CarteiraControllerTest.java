package com.orange.proposta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.proposta.cartao.*;
import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.criaProposta.Proposta;
import com.orange.proposta.criaProposta.PropostaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
public class CarteiraControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CartaoRepository cartaoRepository;

    @Autowired
    BloqueioRepository bloqueioRepository;

    @Autowired
    PropostaRepository propostaRepository;


    @MockBean
    CartaoClient cartaoClient;

    Proposta proposta = new Proposta("997.285.210-57","vitin@email.com","vitin","rua fidalga",new BigDecimal("2500.0"));
    Cartao cartao = new Cartao(2508532787248333L, LocalDateTime.of(2021, Month.APRIL,24,14,35,30,1),proposta);
    Carteira carteira = new Carteira("vitin@email.com", TipoCarteira.PAYPAL, cartao);

    @DisplayName("Deve cadastrar a carteira")
    @Test
    void teste04() throws Exception{
        propostaRepository.save(proposta);
        cartaoRepository.save(cartao);
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/carteiras/" + cartao.getId())
                .content(json(new CarteiraRequest("vitin@email.com", "PAYPAL")))
                .header("User-Agent","Postman")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @DisplayName("Deve lançar ApiErroException caso já tenha carteira paypal")
    @Test
    void teste05() throws Exception{
        propostaRepository.save(proposta);
        cartao.associaCarteira(new CarteiraRequest("vitin@email.com","PAYPAL"));
        cartaoRepository.save(cartao);
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/carteiras/" + cartao.getId())
                .content(json(new CarteiraRequest("vitin@email.com","PAYPAL")))
                .header("User-Agent","Postman")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResolvedException() instanceof ApiErroException));
    }


    public String json(CarteiraRequest request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
