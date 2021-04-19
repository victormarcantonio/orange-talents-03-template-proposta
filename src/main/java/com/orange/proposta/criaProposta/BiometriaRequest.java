package com.orange.proposta.criaProposta;

import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class BiometriaRequest {

    private UUID id;
    @NotBlank
    private String fingerprint;

    public BiometriaRequest(UUID id, @NotBlank String fingerprint) {
        this.id = UUID.randomUUID();
        this.fingerprint = Base64.getEncoder().encodeToString(fingerprint.getBytes(StandardCharsets.UTF_8));
    }

    public UUID getId() {
        return id;
    }

    public Biometria converter(Cartao cartao){
        return new Biometria(id,fingerprint, cartao);
    }
}
