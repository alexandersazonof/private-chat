package com.sazonov.chatservice.service;

import com.sazonov.chatservice.domain.Message;
import com.sazonov.chatservice.api.rest.exception.RestException;

import java.util.List;

public interface MessageService {

    Message save(Message message);
    void update(Message message);
    void delete(Message message);
    Message findById(Long id) throws RestException;
    List<Message> findByChatId(Long chatId);
}
