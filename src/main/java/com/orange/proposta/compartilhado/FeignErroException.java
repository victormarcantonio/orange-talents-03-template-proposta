package com.orange.proposta.compartilhado;

import org.springframework.http.HttpStatus;

public class FeignErroException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String reason;

    public FeignErroException(HttpStatus httpStatus, String reason) {
        super(reason);
        this.httpStatus = httpStatus;
        this.reason = reason;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getReason() {
        return reason;
    }
}
