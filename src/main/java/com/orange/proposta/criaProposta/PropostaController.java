package com.orange.proposta.criaProposta;

import com.orange.proposta.biometria.Biometria;
import com.orange.proposta.biometria.BiometriaRepository;
import com.orange.proposta.biometria.BiometriaRequest;
import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.CartaoClient;
import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.compartilhado.FeignErroException;
import com.orange.proposta.metrica.MinhasMetricas;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    //1
    private PropostaRepository propostaRepository;
    //1
    private PropostaClient propostaClient;

    private MinhasMetricas minhasMetricas;

    private Tracer tracer;

    public PropostaController(PropostaRepository propostaRepository, PropostaClient propostaClient, MinhasMetricas minhasMetricas, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.propostaClient = propostaClient;
        this.minhasMetricas = minhasMetricas;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.email", request.getEmail());
        activeSpan.log("teste");
        List<Proposta> propostas = propostaRepository.findAll();
        propostas.forEach(proposta -> {
            String docDecrypt = decrypt(proposta.getDocumento());
            if(docDecrypt.equals(request.getDocumento())){
                throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta já cadastrada para este cliente");
            }
        });
        Proposta proposta = request.converter();
        propostaRepository.save(proposta);
        AnaliseResponse analiseResponse = analiseProposta(proposta);
        proposta.aceitaProposta(analiseResponse.getResultadoSolicitacao());
        proposta.setDocumento(encrypt(proposta.getDocumento()));
        propostaRepository.save(proposta);
        minhasMetricas.contador();
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

    private String decrypt(String doc){
       return  Encryptors.text("password", "70617373776f7264").decrypt(doc);
    }

    private String encrypt(String doc){
        return  Encryptors.text("password", "70617373776f7264").encrypt(doc);
    }

}
