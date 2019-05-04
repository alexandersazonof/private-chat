package com.sazonov.chatservice.security.exception;

import com.sazonov.chatservice.rest.exception.RestException;

public class AccessDeniedException extends RestException {

    public AccessDeniedException(){
        super();
    }

    public AccessDeniedException(String message){
        super(message);
    }

    public AccessDeniedException(String message, Exception e){
        super(message, e);
    }

    public AccessDeniedException(Exception e){
        super(e);
    }


}
