package com.orange.proposta.cartao;

import com.orange.proposta.criaProposta.Proposta;
import com.orange.proposta.criaProposta.Status;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Cartao {
    @Id
    private Long id;
    private LocalDateTime emissao;
    @OneToOne
    private Proposta proposta;
    @Enumerated
    private StatusCartao status = StatusCartao.ATIVO;


    //Apenas Hibernate utiliza
    @Deprecated
    public Cartao() {
    }

    public Cartao(Long id, LocalDateTime emissao, Proposta proposta) {
        this.id = id;
        this.emissao = emissao;
        this.proposta = proposta;
    }

    public Long getId() {
        return id;
    }

    public void bloqueia(){
        this.status = StatusCartao.BLOQUEADO;
    }
}
