package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.repository.MessageRepository;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.MessageService;
import com.sazonov.chatservice.service.exception.MessageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SecurityUtil securityUtil;



    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message update(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public boolean delete(Message message) {
        messageRepository.delete(message);

        return true;
    }

    @Override
    public Message findById(Long id) throws RestException {
        return messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException("Message not found"));
    }

    @Override
    public List<Message> findByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
