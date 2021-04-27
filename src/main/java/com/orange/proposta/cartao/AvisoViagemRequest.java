package com.orange.proposta.cartao;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @JsonFormat(pattern= "dd/MM/yyyy")
    @FutureOrPresent
    private LocalDate dataTermino;

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public AvisoViagem converter(String userAgent, String ip, Cartao cartao){
        return new AvisoViagem(destino,dataTermino,ip,userAgent,cartao);
    }
}
