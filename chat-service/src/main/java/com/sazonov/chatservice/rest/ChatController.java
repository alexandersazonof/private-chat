package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("")
    public ResponseEntity getAllByUserId(@RequestParam(required = true) Long id){
        List<Chat> chats = chatService.findByUserId(id);

        return ok(chats);
    }


}
