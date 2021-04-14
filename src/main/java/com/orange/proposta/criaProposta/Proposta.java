package com.orange.proposta.criaProposta;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Proposta {

    @Id
    private UUID id;
    @NotBlank
    private String documento;
    @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull
    @Positive
    private BigDecimal salario;
    @Enumerated(EnumType.STRING)
    private Status status;

    //Usado apenas pelo Hibernate
    @Deprecated
    public Proposta() {
    }

    public Proposta(UUID id, @NotBlank String documento, @NotBlank String email, @NotBlank String nome, @NotBlank String endereco, @NotBlank @Positive BigDecimal salario) {
        this.id = id;
        this.documento = documento;
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

    public boolean aceitaProposta(String resultadoSolicitacao) {
      this.status = resultadoSolicitacao.equals("SEM_RESTRICAO") ? Status.ELEGIVEL : Status.NAO_ELEGIVEL;
      return this.status.equals(Status.ELEGIVEL);
    }
}
