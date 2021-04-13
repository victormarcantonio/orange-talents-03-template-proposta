package com.orange.proposta.compartilhado;

import org.springframework.http.HttpStatus;

public class ApiErroException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String reason;

    public ApiErroException(HttpStatus httpStatus, String reason) {
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
