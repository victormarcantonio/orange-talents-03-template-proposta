package com.orange.proposta.criaProposta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnaliseRequest {
    @NotBlank
    private String documento;
    @NotBlank
    private String nome;
    @NotNull
    private String idProposta;

    public AnaliseRequest(@NotBlank String documento, @NotBlank String nome, @NotNull String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
