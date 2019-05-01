package com.sazonov.chatservice.security.exception;

import com.sazonov.chatservice.rest.exception.RestException;

public class InvalidJwtAuthenticationException extends RestException {

    public InvalidJwtAuthenticationException(){
        super();
    }

    public InvalidJwtAuthenticationException(String message){
        super(message);
    }

    public InvalidJwtAuthenticationException(String message, Exception e){
        super(message, e);
    }

    public InvalidJwtAuthenticationException(Exception e){
        super(e);
    }
}
