package com.orange.proposta.cartao;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @CreationTimestamp
    private LocalDateTime instante;
    private String ip;
    private String userAgent;
    @OneToOne
    private Cartao cartao;

    public Bloqueio() {
    }

    public Bloqueio(String ip, String userAgent, Cartao cartao) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

}
