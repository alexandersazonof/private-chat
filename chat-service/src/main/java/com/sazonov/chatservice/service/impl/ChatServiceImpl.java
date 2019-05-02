package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.repository.ChatRepository;
import com.sazonov.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;


    @Override
    public List<Chat> findByUserId(Long id) {

        List<Chat> chats = chatRepository.findByUserId(id);

        return chats;
    }

    @Override
    public void delete(Chat chat) {
        chatRepository.delete(chat);
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Chat update(Chat chat) {
        return chatRepository.save(chat);
    }
}
