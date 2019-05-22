package com.sazonov.chatservice.service;

import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.api.rest.exception.RestException;

import java.util.List;

public interface ChatService {

    List<Chat> findByUserId(Long id);
    void delete(Chat chat);
    Chat save(Chat chat);
    void update(Chat chat);
    Chat findById(Long id) throws RestException;
    List<Chat> findByUserId(Long userId, String value);
}
