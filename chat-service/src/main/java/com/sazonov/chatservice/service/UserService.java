package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;

import java.util.List;

public interface UserService {

    User signUp(User user) throws RestException;
    String login(User user) throws RestException;
    User findById(Long id) throws RestException;
    User findByLogin(String login) throws RestException;
    List<User> findAll() throws RestException;
}
