package com.orange.proposta.criaProposta;

import com.orange.proposta.compartilhado.ApiErroException;
import com.orange.proposta.compartilhado.FeignErroException;
import feign.FeignException;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.BindException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    //1
    private PropostaRepository propostaRepository;
    private CartaoRepository cartaoRepository;
    private BiometriaRepository biometriaRepository;
    //2
    private PropostaClient propostaClient;
    private CartaoClient cartaoClient;

    public PropostaController(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, BiometriaRepository biometriaRepository, PropostaClient propostaClient, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
        this.propostaClient = propostaClient;
        this.cartaoClient = cartaoClient;
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
    public ResponseEntity<PropostaResponse> detalheProposta(@PathVariable ("idProposta") Long idProposta){
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

    @PostMapping("/biometria/{idCartao}")
    public ResponseEntity<?>cadastrarBiometria(@PathVariable ("idCartao") Long idCartao, @RequestBody BiometriaRequest request, UriComponentsBuilder uriBuilder){
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);
        if(cartao.isPresent()){
          Biometria biometria = request.converter(cartao.get());
          biometriaRepository.save(biometria);
          URI uri = uriBuilder.path("/propostas/biometria/{id}").buildAndExpand(request.getId()).toUri();
          return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }
}
