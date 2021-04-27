package com.orange.proposta.cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {

    @Email
    @NotBlank
    private String email;

    private String carteira;

    public CarteiraRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }
    public String getCarteira() {
        return carteira;
    }


    public Carteira converter(Cartao cartao){
        return new Carteira(email, Enum.valueOf(TipoCarteira.class,carteira), cartao);
    }
}
