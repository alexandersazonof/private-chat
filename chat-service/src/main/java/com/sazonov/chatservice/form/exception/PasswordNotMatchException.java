package com.sazonov.chatservice.form.exception;

import com.sazonov.chatservice.rest.exception.RestException;

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
