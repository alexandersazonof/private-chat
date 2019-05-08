package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.ChatForm;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.ResponseMessage;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/chats")
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;



    @GetMapping("")
    public ResponseEntity getAllByUserId(@RequestParam(required = true) Long userId) throws RestException {
        log.info("Get information about chats by user id: " + userId.toString());

        securityUtil.userHasAccess(userId);

        List<Chat> chats = chatService.findByUserId(userId).stream().map(chat -> {
             securityUtil.deletePassword(chat.getUsers());
             return chat;
        }).collect(Collectors.toList());

        return ok(
                ResponseMessage.builder()
                .put("success", true)
                .put("chats", chats)
        );
    }

    @PostMapping("")
    public ResponseEntity createChat(@RequestBody @Valid ChatForm chatForm) throws RestException {
        log.info("Create new chat: " + chatForm);

        List<User> users = chatForm.getMembers().stream().map(value -> {
            try {

                User user = userService.findById(value);
                return user;
            } catch (RestException e) {
                log.warn("Can not throws exception");
                return new User();
            }
        }).collect(Collectors.toList());

        Chat chat = Chat.builder()
                .name(chatForm.getName())
                .users(users)
                .build();

        chatService.save(chat);

        return ok(
                ResponseMessage.builder()
                .put("success", true)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity getChatById(@PathVariable Long id) throws RestException {
        log.info("Get information about chat by id: " + id.toString());

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.deletePassword(chat);

        securityUtil.userExistInChat(user ,chat);

        return ok(
                ResponseMessage.builder()
                        .put("success", true)
                        .put("chat", chat)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteChatById(@PathVariable Long id) throws RestException {
        log.info("Delete chat by id: " + id.toString());


        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user ,chat);

        chatService.delete(chat);

        return ok(
                ResponseMessage.builder()
                        .put("success", true)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity editChatById(@PathVariable Long id,
                                       @RequestBody @Valid ChatForm chatForm) throws RestException {

        log.info("Edit chat data by chat id: " + id.toString());

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


        return ok(
                ResponseMessage.builder()
                        .put("success", true)
        );
    }
}
