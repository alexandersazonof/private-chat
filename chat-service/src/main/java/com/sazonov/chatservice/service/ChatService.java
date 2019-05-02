package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.Chat;

import java.util.List;

public interface ChatService {

    List<Chat> findByUserId(Long id);
    void delete(Chat chat);
    Chat save(Chat chat);
    Chat update(Chat chat);
}
