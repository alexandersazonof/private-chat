package com.sazonov.chatservice.service.exception;

import com.sazonov.chatservice.api.rest.exception.RestException;

public class MessageNotFoundException extends RestException {

    public MessageNotFoundException(){
        super();
    }

    public MessageNotFoundException(String message){
        super(message);
    }

    public MessageNotFoundException(String message, Exception e){
        super(message, e);
    }

    public MessageNotFoundException(Exception e){
        super(e);
    }
}
