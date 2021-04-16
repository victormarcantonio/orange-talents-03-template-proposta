package com.orange.proposta.criaProposta;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public class PropostaRequest {
    private UUID id;
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
        this.id = UUID.randomUUID();
        this.documento = documento.replace(".", "").replace("-","");
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }
    
    public UUID getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Proposta converter(){
        return new Proposta(id,documento, email, nome, endereco, salario);
    }
}
