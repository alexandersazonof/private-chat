package com.sazonov.chatservice.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResponseMessage {

    private HashMap response;

    public ResponseMessage(){
        response = new HashMap();
    }

    public static ResponseMessage builder(){
        return new ResponseMessage();
    }

    public ResponseMessage put(Object key, Object value) {

        response.put(key, value);
        return this;
    }
}
