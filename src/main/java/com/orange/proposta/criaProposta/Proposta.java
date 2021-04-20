package com.orange.proposta.criaProposta;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Proposta {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
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
    private Status status = Status.NAO_ELEGIVEL;
    @OneToOne(mappedBy = "proposta")
    private Cartao cartao;

    //Usado apenas pelo Hibernate
    @Deprecated
    public Proposta() {
    }

    public Proposta(@NotBlank String documento, @NotBlank String email, @NotBlank String nome, @NotBlank String endereco, @NotBlank @Positive BigDecimal salario) {
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

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Status getStatus() {
        return status;
    }

    public void aceitaProposta(String resultadoSolicitacao) {
        this.status = resultadoSolicitacao.equals("SEM_RESTRICAO") ? Status.ELEGIVEL : Status.NAO_ELEGIVEL;
    }


    public Cartao criaCartao(CartaoResponse cartaoResponse) {
       return new Cartao(Long.parseLong(cartaoResponse.getId().replace("-", "")), LocalDateTime.parse(cartaoResponse.getEmitidoEm()), this);
    }
}
