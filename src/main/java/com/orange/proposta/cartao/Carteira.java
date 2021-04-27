package com.orange.proposta.cartao;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Carteira {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @NotBlank
    private String email;
    private TipoCarteira carteira;
    @ManyToOne
    private Cartao cartao;


    //Apenas Hibernate da massa usa
    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotBlank String email, @NotBlank TipoCarteira carteira, Cartao cartao) {
        this.email = email;
        this.carteira = carteira;
        this.cartao = cartao;
    }

    public UUID getId() {
        return id;
    }

    public boolean retornaCarteira(TipoCarteira tipoCarteira){
        return this.carteira.equals(tipoCarteira);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carteira)) return false;

        Carteira carteira1 = (Carteira) o;

        if (getId() != null ? !getId().equals(carteira1.getId()) : carteira1.getId() != null) return false;
        if (email != null ? !email.equals(carteira1.email) : carteira1.email != null) return false;
        if (carteira != carteira1.carteira) return false;
        return cartao != null ? cartao.equals(carteira1.cartao) : carteira1.cartao == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (carteira != null ? carteira.hashCode() : 0);
        result = 31 * result + (cartao != null ? cartao.hashCode() : 0);
        return result;
    }
}
