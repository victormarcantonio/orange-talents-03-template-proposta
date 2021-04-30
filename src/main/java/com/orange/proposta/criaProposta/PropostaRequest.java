package com.orange.proposta.criaProposta;


import org.springframework.security.crypto.encrypt.Encryptors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public class PropostaRequest {
    @NotBlank
    private String documento;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull
    @Positive
    private BigDecimal salario;

    public PropostaRequest(Long id, @NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento.replace(".", "").replace("-","");
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }


    public Proposta converter(){
        return new Proposta(documento, email, nome, endereco, salario);
    }
}
