package com.orange.proposta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.proposta.cartao.*;
import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.criaProposta.Proposta;
import com.orange.proposta.criaProposta.PropostaRepository;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ActiveProfiles("test")
public class BloqueioControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CartaoRepository cartaoRepository;

    @Autowired
    PropostaRepository propostaRepository;


    @MockBean
    CartaoClient cartaoClient;


    Proposta proposta = new Proposta("997.285.210-57","vitin@email.com","vitin","rua fidalga",new BigDecimal("2500.0"));
    Cartao cartao = new Cartao(2508532787248333L,LocalDateTime.of(2021, Month.APRIL,24,14,35,30,1),proposta);
    Bloqueio bloqueio = new Bloqueio("ip", "user", cartao);


    @DisplayName("Deve bloquear o cartão e retornar status 201")
    @Test
    void teste01() throws Exception{
        propostaRepository.save(proposta);
        cartaoRepository.save(cartao);
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/bloqueio/" + cartao.getId())
        .content(json(new BloqueioRequest()))
        .header("User-Agent","Postman")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }



    @DisplayName("Deve retornar 404 caso o cartão não esteja cadastrado")
    @Test
    void teste02() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/bloqueio/" + cartao.getId())
                .content(json(new BloqueioRequest()))
                .header("User-Agent","Postman")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @DisplayName("Deve retornar Api Exception caso o cartão já esteja bloqueado")
    @Test
    void teste03() throws Exception {
        propostaRepository.save(proposta);
        cartao.bloqueia("123", "teste");
        cartaoRepository.save(cartao);
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/bloqueio/" + cartao.getId())
                .content(json(new BloqueioRequest()))
                .header("User-Agent","Postman")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }
    public String json(BloqueioRequest bloqueioRequest) throws JsonProcessingException {
        bloqueioRequest.setSistemaResponsavel("proposta");
        return mapper.writeValueAsString(bloqueioRequest);
    }

}
