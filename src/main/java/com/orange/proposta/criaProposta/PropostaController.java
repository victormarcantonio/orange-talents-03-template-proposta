package com.orange.proposta.criaProposta;

import com.orange.proposta.compartilhado.ApiErroException;
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

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }


    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Proposta proposta = request.converter();
        boolean existeDocumento = propostaRepository.existsByDocumento(request.getDocumento());
        if(existeDocumento) {
          throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe proposta criada para o cliente de documento "+request.getDocumento()+"");
        }
        propostaRepository.save(proposta);
        URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }
}
