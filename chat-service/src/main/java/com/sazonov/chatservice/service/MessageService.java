package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.service.exception.MessageNotFoundException;

import java.util.List;

public interface MessageService {

    Message save(Message message);
    Message update(Message message);
    boolean delete(Message message);
    Message findById(Long id) throws RestException;
    List<Message> findByChatId(Long chatId);
}
