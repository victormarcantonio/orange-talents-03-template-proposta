package com.orange.proposta.criaProposta;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Cartao {
    @Id
    private Long numero;
    private LocalDateTime emissao;
    @OneToOne
    private Proposta proposta;

    //Apenas Hibernate utiliza
    @Deprecated
    public Cartao() {
    }

    public Cartao(Long numero, LocalDateTime emissao, Proposta proposta) {
        this.numero = numero;
        this.emissao = emissao;
        this.proposta = proposta;
    }
}
