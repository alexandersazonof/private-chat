package com.sazonov.chatservice.api.rest.exception;

public class RestException extends Exception {

    protected RestException(){
        super();
    }

    protected RestException(String message){
        super(message);
    }

    protected RestException(String message, Exception e){
        super(message, e);
    }

    protected RestException(Exception e){
        super(e);
    }
}
