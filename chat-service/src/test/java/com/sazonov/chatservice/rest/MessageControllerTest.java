package com.sazonov.chatservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sazonov.chatservice.dto.MessageDto;
import com.sazonov.chatservice.dto.ChatDto;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.security.provider.JwtTokenProvider;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.impl.ChatServiceImpl;
import com.sazonov.chatservice.service.impl.MessageServiceImpl;
import com.sazonov.chatservice.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MessageController messageController;

    @MockBean
    private MessageServiceImpl messageService;

    @MockBean
    private ChatServiceImpl chatService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private SecurityUtil securityUtil;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WebApplicationContext webApplicationContext;


    private User user;
    private ChatDto chatDto;
    private Chat chat;
    private List<Chat> chats;
    private Message message;
    private MessageDto messageDto;
    private List<Message> messages;
    private ObjectMapper objectMapper;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        objectMapper = new ObjectMapper();

        user = User.builder()
                .id(1L)
                .name("maga")
                .password("password")
                .login("login")
                .roles(Arrays.asList("ROLE_USER"))
                .build();

        chatDto = ChatDto.builder()
                .name("mychat")
                .members(Arrays.asList(Long.valueOf(1), 1L))
                .build();

        chat = Chat.builder()
                .id(1L)
                .name(chatDto.getName())
                .users(Arrays.asList(user))
                .build();

        chats = Arrays.asList(chat);

        message = Message.builder()
                .id(1L)
                .chat(chat)
                .date(new Date())
                .user(user)
                .value("Shalom")
                .build();

        messages = Arrays.asList(message);

        messageDto = MessageDto.builder()
                .userId(1L)
                .value("qq")
                .build();
    }

    @Test
    public void getAllMessageByChatId() throws Exception {
        when(chatService.findById(chat.getId())).thenReturn(chat);
        when(messageService.findByChatId(chat.getId())).thenReturn(messages);

        mockMvc.perform(
                get(String.format("/api/v1/chats/%d/messages", chat.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.messages[0].id").value(messages.get(0).getId()))
                .andExpect(jsonPath("response.messages[0].value").value(messages.get(0).getValue()));
    }

    @Test
    public void getAllMessageByChatIdWithoutId() throws Exception {

        mockMvc.perform(
                get(String.format("/api/v1/chats/%s/messages", "asd")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getMessageById() throws Exception {
        when(chatService.findById(chat.getId())).thenReturn(chat);
        when(messageService.findById(message.getId())).thenReturn(message);

        mockMvc.perform(
                get(String.format("/api/v1/chats/%d/messages/%d", chat.getId(), message.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.messages.id").value(message.getId()))
                .andExpect(jsonPath("response.messages.value").value(message.getValue()));
    }

    @Test
    public void getMessageByIdWithoutMessageId() throws Exception {
        mockMvc.perform(
                get(String.format("/api/v1/chats/%d/messages/%s", chat.getId(), "asd")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createMessageWithCorrectValue() throws Exception {
        when(chatService.findById(chat.getId())).thenReturn(chat);

        mockMvc.perform(
                post(String.format("/api/v1/chats/%d/messages", chat.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(messageDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void createMessageWithoutValue() throws Exception {
        when(chatService.findById(chat.getId())).thenReturn(chat);

        mockMvc.perform(
                post(String.format("/api/v1/chats/%d/messages", chat.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteMessage() throws Exception {
        when(messageService.findById(message.getId())).thenReturn(message);

        mockMvc.perform(
                delete(String.format("/api/v1/chats/%d/messages/%d", chat.getId(), message.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMessageWithoutId() throws Exception {
        when(messageService.findById(message.getId())).thenReturn(message);

        mockMvc.perform(
                delete(String.format("/api/v1/chats/%d/messages/%s", chat.getId(), "asd")))
                .andExpect(status().isBadRequest());
    }

}
