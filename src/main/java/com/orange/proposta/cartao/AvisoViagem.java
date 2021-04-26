package com.orange.proposta.cartao;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @NotBlank
    private String destino;
    @CreationTimestamp
    private LocalDateTime instante;
    @FutureOrPresent
    private LocalDate localDate;
    private String ip;
    private String userAgent;
    @ManyToOne
    private Cartao cartao;

    //Usado apenas pelo hibernate
    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(@NotBlank String destino, @FutureOrPresent LocalDate localDate, String ip, String userAgent, Cartao cartao) {
        this.destino = destino;
        this.localDate = localDate;
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public UUID getId() {
        return id;
    }
}
