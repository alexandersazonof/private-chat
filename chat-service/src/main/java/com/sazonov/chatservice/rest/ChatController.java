package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("")
    public ResponseEntity getAllByUserId(){
        List<Chat> chats = chatRepository.findByUserId(1L);

        return ok(chats);
    }
}
