package com.sazonov.chatservice.service.exception;

import com.sazonov.chatservice.api.rest.exception.RestException;

public class ChatNotFoundException extends RestException {


    public ChatNotFoundException(){
        super();
    }

    public ChatNotFoundException(String message){
        super(message);
    }

    public ChatNotFoundException(String message, Exception e){
        super(message, e);
    }

    public ChatNotFoundException(Exception e){
        super(e);
    }
}
