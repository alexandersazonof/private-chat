package com.sazonov.chatservice.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class ApiMessage {

    private HashMap response;

    public ApiMessage(){
        response = new HashMap();
    }

    public static ApiMessage builder(){
        return new ApiMessage();
    }

    public ApiMessage put(Object key, Object value) {

        response.put(key, value);
        return this;
    }
}
