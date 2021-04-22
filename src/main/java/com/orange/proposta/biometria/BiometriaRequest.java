package com.orange.proposta.biometria;

import com.orange.proposta.biometria.Biometria;
import com.orange.proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class BiometriaRequest {


    @NotBlank
    private String fingerprint;

    public void setFingerprint(String fingerprint) {
        this.fingerprint = Base64.getEncoder().encodeToString(fingerprint.getBytes(StandardCharsets.UTF_8));
    }

    public Biometria converter(Cartao cartao){
        return new Biometria(fingerprint, cartao);
    }
}
