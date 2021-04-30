package com.orange.proposta.criaProposta;


import com.orange.proposta.cartao.Cartao;
import com.orange.proposta.cartao.CartaoResponse;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    public boolean verificaDocumento(String nrDocumento){
        return this.documento.equals(nrDocumento);
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proposta)) return false;

        Proposta proposta = (Proposta) o;

        if (getId() != null ? !getId().equals(proposta.getId()) : proposta.getId() != null) return false;
        if (getDocumento() != null ? !getDocumento().equals(proposta.getDocumento()) : proposta.getDocumento() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(proposta.getEmail()) : proposta.getEmail() != null) return false;
        if (getNome() != null ? !getNome().equals(proposta.getNome()) : proposta.getNome() != null) return false;
        if (getEndereco() != null ? !getEndereco().equals(proposta.getEndereco()) : proposta.getEndereco() != null)
            return false;
        if (getSalario() != null ? !getSalario().equals(proposta.getSalario()) : proposta.getSalario() != null)
            return false;
        if (getStatus() != proposta.getStatus()) return false;
        return cartao != null ? cartao.equals(proposta.cartao) : proposta.cartao == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDocumento() != null ? getDocumento().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getEndereco() != null ? getEndereco().hashCode() : 0);
        result = 31 * result + (getSalario() != null ? getSalario().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (cartao != null ? cartao.hashCode() : 0);
        return result;
    }
}
