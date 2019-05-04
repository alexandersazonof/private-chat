package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.ChatForm;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private SecurityUtil securityUtil;



    @GetMapping("")
    public ResponseEntity getAllByUserId(@RequestParam(required = true) Long userId) throws RestException {

        securityUtil.userHasAccess(userId);

        List<Chat> chats = chatService.findByUserId(userId);
        return ok(chats);
    }

    @PostMapping("")
    public ResponseEntity createChat(@RequestBody ChatForm chatForm) throws RestException {

        List<User> users = new ArrayList<>();

        for (Long i :chatForm.getMembers()) {
            users.add(userService.findById(i));
        }

        Chat chat = Chat.builder()
                .name(chatForm.getName())
                .users(users)
                .build();

        chatService.save(chat);

        Map response = new HashMap();

        response.put("success", true);

        return ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity getChatById(@PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user ,chat);

        Map response = new HashMap();

        response.put("success", true);
        response.put("chat", chat);

        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteChatById(@PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user ,chat);

        chatService.delete(chat);

        Map response = new HashMap();

        response.put("success", true);

        return ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity editChatById(@PathVariable Long id,
                                       @RequestBody ChatForm chatForm) throws RestException {


        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        List<User> users = new ArrayList<>();
        for (Long i :chatForm.getMembers()) {
            users.add(userService.findById(i));
        }


        Chat updateChat = Chat.builder()
                .id(id)
                .name(chatForm.getName())
                .users(users)
                .build();

        chatService.update(updateChat);

        Map response = new HashMap();

        response.put("success", true);

        return ok(response);
    }
}
