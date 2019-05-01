package com.sazonov.chatservice.service.exception;

import com.sazonov.chatservice.rest.exception.RestException;

public class UserExistsException extends RestException {

    public UserExistsException(){
        super();
    }

    public UserExistsException(String message){
        super(message);
    }

    public UserExistsException(String message, Exception e){
        super(message, e);
    }

    public UserExistsException(Exception e){
        super(e);
    }
}
