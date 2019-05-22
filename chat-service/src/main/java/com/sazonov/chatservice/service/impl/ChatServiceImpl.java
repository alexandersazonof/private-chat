package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.repository.ChatRepository;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.exception.ChatNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> findByUserId(Long id) { return chatRepository.findByUserId(id); }

    @Override
    public void delete(Chat chat) {
        chatRepository.delete(chat);
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public void update(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public Chat findById(Long id) throws RestException {
        return chatRepository.findById(id).orElseThrow(() -> new ChatNotFoundException(MessageResponse.CHAT_NOT_FOUND));
    }

    @Override
    public List<Chat> findByUserId(Long userId, String value) {
        if (value == null || value.isEmpty()) {
            return chatRepository.findByUserId(userId);
        }
        return chatRepository.findByUserId(userId, value);
    }
}
