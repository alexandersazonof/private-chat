package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.dto.MessageDto;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.MessageService;
import com.sazonov.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/chats/{chatId}/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;



    @GetMapping("")
    public ResponseEntity getMessagesByChatId(@PathVariable Long chatId) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        List<Message> messages = messageService.findByChatId(chatId);

        HashMap response = new HashMap();

        response.put("messages", messages);

        return ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMessageById(@PathVariable Long chatId,
                                         @PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        Message message = messageService.findById(id);

        HashMap response = new HashMap();
        response.put("message", message);

        return ok(response);
    }

    @PostMapping("")
    public ResponseEntity createMessage(@PathVariable Long chatId,
                                        @RequestBody @Valid MessageDto messageDto) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        Message message = Message.builder()
                .value(messageDto.getValue())
                .date(messageDto.getDate())
                .user(userService.findById(messageDto.getUserId()))
                .chat(chatService.findById(messageDto.getChatId()))
                .build();

        messageService.save(message);

        HashMap response = new HashMap();
        response.put("success", true);

        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMessageById(@PathVariable Long chatId,
                                            @PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(chatId);

        User user = securityUtil.getUserFromSecurityContext();
        Message message = messageService.findById(id);

        securityUtil.userHasAccessToMessage(user, message);

        messageService.delete(message);

        HashMap response = new HashMap();
        response.put("success", true);

        return ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity editMessageById(@PathVariable Long chatId,
                                          @PathVariable Long id,
                                          @RequestBody @Valid MessageDto messageDto) throws RestException {

        Chat chat = chatService.findById(chatId);

        User user = securityUtil.getUserFromSecurityContext();
        Message messageCheck = messageService.findById(id);

        securityUtil.userHasAccessToMessage(user, messageCheck);

        Message message = Message.builder()
                .id(id)
                .value(messageDto.getValue())
                .date(messageDto.getDate())
                .user(userService.findById(messageDto.getUserId()))
                .chat(chatService.findById(messageDto.getChatId()))
                .build();

        messageService.update(message);

        HashMap response = new HashMap();
        response.put("success", true);

        return ok(response);
    }
}
