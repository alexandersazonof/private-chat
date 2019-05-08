package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.repository.MessageRepository;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.impl.MessageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private SecurityUtil securityUtil;

    private Chat chat;
    private User user;
    private Message message;
    private List<User> users;
    private List<Chat> chats;
    private List<Message> messages;

    @Before
    public void init(){
        user = User.builder()
                .id(1L)
                .login("login1")
                .password("password")
                .name("maga")
                .roles(Arrays.asList("ROLE_USER"))
                .build();


        users = Arrays.asList(user);


        chat = Chat.builder()
                .id(1L)
                .name("Your chat")
                .users(users)
                .build();

        chats = Arrays.asList(chat);

        message = Message.builder()
                .id(1L)
                .value("value")
                .date(new Date())
                .chat(chat)
                .user(user)
                .build();

        messages = Arrays.asList(message);
    }

    @Test
    public void findByIdTest() throws RestException {
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        Message actualMessage = messageService.findById(message.getId());

        assertEquals(actualMessage, message);
    }

    @Test
    public void findByChatIdTest() {
        when(messageRepository.findByChatId(chat.getId())).thenReturn(messages);

        List<Message> actualMessages = messageService.findByChatId(chat.getId());

        assertEquals(actualMessages, messages);
    }

    @Test
    public void saveTest(){
        when(messageRepository.save(message)).thenReturn(message);

        Message actualMessage = messageService.save(message);

        assertEquals(actualMessage, message);
    }
}
