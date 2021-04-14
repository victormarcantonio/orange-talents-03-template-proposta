package com.orange.proposta.compartilhado;

import feign.FeignException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandler {

    private MessageSource messageSource;

    public ValidationErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDto handleValidationError(MethodArgumentNotValidException exception){
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return buildValidationErrors(globalErrors, fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ValidationErrorsOutputDto handleValidationError(BindException exception){
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return buildValidationErrors(globalErrors, fieldErrors);
    }

    @ExceptionHandler(FeignErroException.class)
    public ResponseEntity<ErroPadronizado> handleValidationError(FeignErroException exception){
        Collection<String>mensagens = new ArrayList<>();
        mensagens.add(exception.getReason());

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(exception.getHttpStatus()).body(erroPadronizado);
    }

    @ExceptionHandler(ApiErroException.class)
    public ResponseEntity<ErroPadronizado>handleApiErroException(ApiErroException apiErroException){
        Collection<String>mensagens = new ArrayList<>();
        mensagens.add(apiErroException.getReason());

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(apiErroException.getHttpStatus()).body(erroPadronizado);
    }

    private ValidationErrorsOutputDto buildValidationErrors(List<ObjectError> globalErrors, List<FieldError> fieldErrors){
       ValidationErrorsOutputDto validationErrors = new ValidationErrorsOutputDto();
       globalErrors.forEach(error-> validationErrors.addError(getErrorMessage(error)));
       fieldErrors.forEach(error-> {
           String errorMessage = getErrorMessage(error);
           validationErrors.addFieldError(error.getField(), errorMessage);

       });
       return validationErrors;
    }

    private String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }

}
