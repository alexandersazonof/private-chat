package com.sazonov.chatservice.api.rest;

import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.domain.ApiMessage;
import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.domain.Message;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.dto.MessageDto;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.MessageService;
import com.sazonov.chatservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api/v1/chats/{chatId}/messages")
@Api(value = "Message management system")
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    public MessageController(MessageService messageService,ChatService chatService, UserService userService, SecurityUtil securityUtil) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @GetMapping
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

        securityUtil.deletePassword(chat.getUsers());


        return ok(
                ApiMessage.builder()
                .put(MessageResponse.RESPONSE_SUCCESS, true)
                .put("messages", messages)
                .put("chat", chat)
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
                .put(MessageResponse.RESPONSE_SUCCESS, true)
                .put("messages", message));
    }

    @PostMapping
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

        message = messageService.save(message);

        securityUtil.deletePassword(message.getUser());
        securityUtil.deletePassword(message.getChat());



        return ok(
                ApiMessage.builder()
                .put(MessageResponse.RESPONSE_SUCCESS, true)
                .put("message", message)
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

        return ok(ApiMessage.builder()
                        .put(MessageResponse.RESPONSE_SUCCESS, true));
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
                        .put(MessageResponse.RESPONSE_SUCCESS, true)
        );
    }
}
