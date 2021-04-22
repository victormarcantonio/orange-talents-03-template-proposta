package com.orange.proposta.biometria;

import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class BiometriaController {

    private CartaoRepository cartaoRepository;
    private BiometriaRepository biometriaRepository;

    public BiometriaController(CartaoRepository cartaoRepository, BiometriaRepository biometriaRepository) {
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
    }

    @PostMapping("/biometria/{idCartao}")
    public ResponseEntity<?> cadastrarBiometria(@PathVariable("idCartao") Long idCartao, @RequestBody BiometriaRequest request, UriComponentsBuilder uriBuilder){
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);
        if(cartao.isPresent()){
            Biometria biometria = request.converter(cartao.get());
            biometriaRepository.save(biometria);
            URI uri = uriBuilder.path("/propostas/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }
}
