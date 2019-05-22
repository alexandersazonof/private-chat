package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.domain.Message;
import com.sazonov.chatservice.repository.MessageRepository;
import com.sazonov.chatservice.service.MessageService;
import com.sazonov.chatservice.service.exception.MessageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl (MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void update(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public Message findById(Long id) throws RestException {
        return messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException(MessageResponse.MESSAGE_NOT_FOUND));
    }

    @Override
    public List<Message> findByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
