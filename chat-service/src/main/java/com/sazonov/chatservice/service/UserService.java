package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;

public interface UserService {

    User signUp(User user) throws RestException;
    String login(User user) throws RestException;
    User findById(Long id) throws RestException;
    User findByLogin(String login) throws RestException;
}
