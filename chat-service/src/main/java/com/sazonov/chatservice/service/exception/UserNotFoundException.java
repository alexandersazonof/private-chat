package com.sazonov.chatservice.service.exception;

import com.sazonov.chatservice.api.rest.exception.RestException;

public class UserNotFoundException extends RestException {


    public UserNotFoundException(){
        super();
    }

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message, Exception e){
        super(message, e);
    }

    public UserNotFoundException(Exception e){
        super(e);
    }
}
