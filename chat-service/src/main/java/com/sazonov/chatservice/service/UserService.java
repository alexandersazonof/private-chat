package com.sazonov.chatservice.service;

import com.sazonov.chatservice.form.UserForm;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.service.exception.UserExistsException;

import java.util.HashMap;

public interface UserService {

    User signUp(User user) throws RestException;
    HashMap login(User user) throws RestException;
    User findById(Long id) throws RestException;
}
