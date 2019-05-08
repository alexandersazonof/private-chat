package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.dto.MessageDto;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.ResponseMessage;
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
import java.util.Date;
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

        messages.stream().forEach(message -> {
            securityUtil.deletePassword(message.getUser());
            securityUtil.deletePassword(message.getChat().getUsers());
        });


        return ok(
                ResponseMessage.builder()
                .put("success", true)
                .put("messages", messages)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getMessageById(@PathVariable Long chatId,
                                         @PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        Message message = messageService.findById(id);
        securityUtil.deletePassword(message.getUser());
        securityUtil.deletePassword(message.getChat().getUsers());


        return ok( ResponseMessage.builder()
                .put("success", true)
                .put("messages", message));
    }

    @PostMapping("")
    public ResponseEntity createMessage(@PathVariable Long chatId,
                                        @RequestBody @Valid MessageDto messageDto) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        Message message = Message.builder()
                .value(messageDto.getValue())
                .date(new Date())
                .user(userService.findById(messageDto.getUserId()))
                .chat(chatService.findById(chatId))
                .build();

        messageService.save(message);



        return ok(
                ResponseMessage.builder()
                .put("success", true)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMessageById(@PathVariable Long chatId,
                                            @PathVariable Long id) throws RestException {

        User user = securityUtil.getUserFromSecurityContext();
        Message message = messageService.findById(id);

        securityUtil.userHasAccessToMessage(user, message);

        messageService.delete(message);

        return ok(
                ResponseMessage.builder()
                        .put("success", true)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity editMessageById(@PathVariable Long chatId,
                                          @PathVariable Long id,
                                          @RequestBody @Valid MessageDto messageDto) throws RestException {


        User user = securityUtil.getUserFromSecurityContext();
        Message messageCheck = messageService.findById(id);

        securityUtil.userHasAccessToMessage(user, messageCheck);

        Message message = Message.builder()
                .id(id)
                .value(messageDto.getValue())
                .date(messageCheck.getDate())
                .user(userService.findById(messageDto.getUserId()))
                .chat(chatService.findById(chatId))
                .build();

        messageService.update(message);

        return ok(
                ResponseMessage.builder()
                        .put("success", true)
        );
    }
}
