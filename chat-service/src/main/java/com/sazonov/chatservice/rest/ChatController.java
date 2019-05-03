package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.ChatForm;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity getAllByUserId(@RequestParam(required = true) Long id){
        List<Chat> chats = chatService.findByUserId(id);

        return ok(chats);
    }

    @PostMapping("")
    public ResponseEntity createChat(@RequestBody ChatForm chatForm) throws RestException {


        Chat chat = Chat.builder()
                .name(chatForm.getName())
                .build();

        chatService.save(chat);

        Map response = new HashMap();

        response.put("success", true);

        return ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity getChatById(@PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(id);

        Map response = new HashMap();

        response.put("success", true);
        response.put("chat", chat);

        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteChatById(@PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(id);

        chatService.delete(chat);

        Map response = new HashMap();

        response.put("success", true);

        return ok(response);
    }
}
