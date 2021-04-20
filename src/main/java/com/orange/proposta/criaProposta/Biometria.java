package com.orange.proposta.criaProposta;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Biometria(UUID id, String fingerprint, Cartao cartao) {
        this.id = id;
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }
}
