package com.orange.proposta.cartao;

import javax.validation.constraints.NotBlank;

public class BloqueioRequest {

    @NotBlank
    private String sistemaResponsavel;

    public void setSistemaResponsavel(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

}
