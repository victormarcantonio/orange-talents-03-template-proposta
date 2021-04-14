package com.orange.proposta.criaProposta;

import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.compartilhado.FeignErroException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.BindException;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private PropostaRepository propostaRepository;
    private PropostaClient propostaClient;
    private final Logger logger = LoggerFactory.getLogger(AnaliseResponse.class);

    public PropostaController(PropostaRepository propostaRepository, PropostaClient propostaClient) {
        this.propostaRepository = propostaRepository;
        this.propostaClient = propostaClient;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Proposta proposta = request.converter();
        boolean existeDocumento = propostaRepository.existsByDocumento(request.getDocumento());
        if(existeDocumento) {
          throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe proposta criada para o cliente de documento "+request.getDocumento());
        }
        try {
            AnaliseResponse resultadoAnalise = resultado(proposta);
            proposta.aceitaProposta(resultadoAnalise.getResultadoSolicitacao());
            propostaRepository.save(proposta);
            URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(request.getId()).toUri();
            return ResponseEntity.created(uri).body(request);
        }catch(FeignException e){
            propostaRepository.save(proposta);
            throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta não elegível");
        }
    }

    public AnaliseResponse resultado(Proposta proposta){
        AnaliseRequest analiseRequest = new AnaliseRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId().toString());

        return propostaClient.analisaProposta(analiseRequest);
    }
}
