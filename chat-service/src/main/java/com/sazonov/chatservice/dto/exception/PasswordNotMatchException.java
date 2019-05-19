package com.sazonov.chatservice.dto.exception;

import com.sazonov.chatservice.api.rest.exception.RestException;

public class PasswordNotMatchException extends RestException {


    public PasswordNotMatchException(){
        super();
    }

    public PasswordNotMatchException(String message){
        super(message);
    }

    public PasswordNotMatchException(String message, Exception e){
        super(message, e);
    }

    public PasswordNotMatchException(Exception e){
        super(e);
    }
}
