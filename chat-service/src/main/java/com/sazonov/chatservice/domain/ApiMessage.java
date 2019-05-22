package com.sazonov.chatservice.domain;

import lombok.Data;

import java.util.HashMap;

@Data
public class ApiMessage {

    private HashMap<Object, Object> response;

    private ApiMessage(){
        response = new HashMap<>();
    }

    public static ApiMessage builder(){
        return new ApiMessage();
    }

    public ApiMessage put(Object key, Object value) {

        response.put(key, value);
        return this;
    }
}
