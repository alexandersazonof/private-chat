package com.sazonov.chatservice.api.rest.exception;

public class RestException extends Exception {

    public RestException(){
        super();
    }

    public RestException(String message){
        super(message);
    }

    public RestException(String message, Exception e){
        super(message, e);
    }

    public RestException(Exception e){
        super(e);
    }
}
