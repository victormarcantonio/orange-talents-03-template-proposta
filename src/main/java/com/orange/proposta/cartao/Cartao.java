package com.orange.proposta.cartao;

import com.orange.proposta.criaProposta.Proposta;
import com.orange.proposta.criaProposta.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Cartao {
    @Id
    private Long id;
    private LocalDateTime emissao;
    @OneToOne
    private Proposta proposta;
    @Enumerated
    private StatusCartao status = StatusCartao.ATIVO;
    @OneToOne(mappedBy = "cartao")
    private Bloqueio bloqueio;
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Set<Carteira> carteiras = new HashSet<>();


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

    public boolean temBloqueio(){
        return this.bloqueio != null;
    }

    public boolean possuiCarteira(String tipo){
       return this.carteiras.stream().anyMatch(carteira -> carteira.retornaCarteira(Enum.valueOf(TipoCarteira.class,tipo)));
    }

}
