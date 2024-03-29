package com.sazonov.chatservice.api.rest;

import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.dto.ChatDto;
import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.domain.ApiMessage;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.ChatService;
import com.sazonov.chatservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/api/v1/chats")
@Slf4j
@Api(value = "Chat management system")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @Autowired
    public ChatController(ChatService chatService, UserService userService, SecurityUtil securityUtil) {
        this.chatService = chatService;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get chats of user")
    public ResponseEntity getAllByUserId(
            @ApiParam(value = "User id") @RequestParam Long userId,
            @RequestParam(required = false) String value) throws RestException {

        log.info("Get information about chats by user id: " + userId.toString());

        securityUtil.userHasAccess(userId);

        List<Chat> chats = chatService.findByUserId(userId, value).stream().peek(chat -> {
             securityUtil.deletePassword(chat.getUsers());
        }).collect(Collectors.toList());

        return ok(
                ApiMessage.builder()
                .put("chats", chats)
                .put(MessageResponse.RESPONSE_SUCCESS, true)
        );
    }

    @PostMapping
    @ApiOperation(value = "Create chat")
    public ResponseEntity createChat(@RequestBody @Valid ChatDto chatDto) {
        log.info("Create new chat: " + chatDto);

        List<User> users = chatDto.getMembers().stream().map(value -> {
            try {
                return userService.findById(value);
            } catch (RestException e) {
                log.warn("Can not throws exception");
                return new User();
            }
        }).collect(Collectors.toList());

        Chat chat = Chat.builder()
                .name(chatDto.getName())
                .users(users)
                .build();

        chatService.save(chat);

        return ok(
                ApiMessage.builder()
                .put(MessageResponse.RESPONSE_SUCCESS, true)
        );
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get information about chat by id")
    public ResponseEntity getChatById(@PathVariable Long id) throws RestException {
        log.info("Get information about chat by id: " + id.toString());

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.deletePassword(chat);

        securityUtil.userExistInChat(user ,chat);

        return ok(
                ApiMessage.builder()
                        .put(MessageResponse.RESPONSE_SUCCESS, true)
                        .put("chat", chat)
        );
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete chat by id")
    public ResponseEntity deleteChatById(@PathVariable Long id) throws RestException {
        log.info("Delete chat by id: " + id.toString());

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user ,chat);

        chatService.delete(chat);

        return ok(
                ApiMessage.builder()
                        .put(MessageResponse.RESPONSE_SUCCESS, true)
        );
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit chat information by id")
    public ResponseEntity editChatById(@PathVariable Long id,
                                       @RequestBody @Valid ChatDto chatDto) throws RestException {

        log.info("Edit chat data by chat id: " + id.toString());

        Chat chat = chatService.findById(id);
        User user = securityUtil.getUserFromSecurityContext();

        securityUtil.userExistInChat(user, chat);

        List<User> users = new ArrayList<>();
        for (Long i : chatDto.getMembers()) {
            users.add(userService.findById(i));
        }


        Chat updateChat = Chat.builder()
                .id(id)
                .name(chatDto.getName())
                .users(users)
                .build();

        chatService.update(updateChat);

        return ok(
                ApiMessage.builder()
                        .put(MessageResponse.RESPONSE_SUCCESS, true)
        );
    }
}
