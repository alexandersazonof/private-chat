package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.rest.exception.RestException;

import java.util.List;

public interface ChatService {

    List<Chat> findByUserId(Long id);
    void delete(Chat chat);
    Chat save(Chat chat);
    Chat update(Chat chat);
    Chat findById(Long id) throws RestException;
}
