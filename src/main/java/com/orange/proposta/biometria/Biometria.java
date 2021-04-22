package com.orange.proposta.biometria;

import com.orange.proposta.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Biometria {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Lob
    private String fingerprint;
    private LocalDate data = LocalDate.now();
    @ManyToOne
    private Cartao cartao;

    public UUID getId() {
        return id;
    }

    //Apenas o Hibernador que usa
    @Deprecated
    public Biometria() {
    }

    public Biometria(String fingerprint, Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }
}
