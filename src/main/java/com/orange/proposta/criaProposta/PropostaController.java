package com.orange.proposta.criaProposta;

import com.orange.proposta.biometria.Biometria;
import com.orange.proposta.biometria.BiometriaRepository;
import com.orange.proposta.biometria.BiometriaRequest;
import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.CartaoClient;
import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.compartilhado.FeignErroException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    //1
    private PropostaRepository propostaRepository;
    //1
    private PropostaClient propostaClient;

    public PropostaController(PropostaRepository propostaRepository, PropostaClient propostaClient, CartaoClient cartaoClient) {
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
        propostaRepository.save(proposta);
        AnaliseResponse analiseResponse = analiseProposta(proposta);
        proposta.aceitaProposta(analiseResponse.getResultadoSolicitacao());
        propostaRepository.save(proposta);
        URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @GetMapping("/{idProposta}")
    public ResponseEntity<PropostaResponse> detalheProposta(@PathVariable ("idProposta") UUID idProposta){
       Optional<Proposta> possivelProposta = propostaRepository.findById(idProposta);
        return possivelProposta.map(proposta -> ResponseEntity.ok().body(new PropostaResponse(proposta)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public AnaliseResponse analiseProposta(Proposta proposta){
        AnaliseRequest analiseRequest = new AnaliseRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId().toString());
        try {
            return  propostaClient.analisaProposta(analiseRequest);
        }catch(FeignException e){
            throw new FeignErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta não elegível");
        }
    }

}
