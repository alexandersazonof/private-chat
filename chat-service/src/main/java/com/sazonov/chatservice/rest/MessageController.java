package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.dto.MessageDto;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.ApiMessage;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.MessageService;
import com.sazonov.chatservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/chats/{chatId}/messages")
@Api(value = "Message management system", description = "Operation with messages")
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
    @ApiOperation(value = "Get messages by chat id")
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
                ApiMessage.builder()
                .put("success", true)
                .put("messages", messages)
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get message information by id")
    public ResponseEntity getMessageById(@PathVariable Long chatId,
                                         @PathVariable Long id) throws RestException {

        Chat chat = chatService.findById(chatId);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        Message message = messageService.findById(id);
        securityUtil.deletePassword(message.getUser());
        securityUtil.deletePassword(message.getChat().getUsers());


        return ok( ApiMessage.builder()
                .put("success", true)
                .put("messages", message));
    }

    @PostMapping("")
    @ApiOperation(value = "Create message")
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
                ApiMessage.builder()
                .put("success", true)
        );
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete message by id")
    public ResponseEntity deleteMessageById(@PathVariable Long chatId,
                                            @PathVariable Long id) throws RestException {

        User user = securityUtil.getUserFromSecurityContext();
        Message message = messageService.findById(id);

        securityUtil.userHasAccessToMessage(user, message);

        messageService.delete(message);

        return ok(
                ApiMessage.builder()
                        .put("success", true)
        );
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit message by id")
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
                ApiMessage.builder()
                        .put("success", true)
        );
    }
}
