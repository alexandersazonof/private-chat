package com.sazonov.chatservice.service;

import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.repository.ChatRepository;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.impl.ChatServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private SecurityUtil securityUtil;

    private Chat chat;
    private User user;
    private List<Chat> chats;

    @Before
    public void init(){

        user = User.builder()
                .id(1L)
                .login("login1")
                .password("password")
                .name("maga")
                .roles(Arrays.asList("ROLE_USER"))
                .build();


        List<User> users = Arrays.asList(user);


        chat = Chat.builder()
                .id(1L)
                .name("Your chat")
                .users(users)
                .build();

        chats = Arrays.asList(chat);
    }


    @Test
    public void findByUserId(){
        when(chatRepository.findByUserId(user.getId())).thenReturn(chats);


        List<Chat> actualChats = chatService.findByUserId(user.getId());

        assertEquals(actualChats.size(), chats.size());
    }

    @Test
    public void saveTest(){
        when(chatRepository.save(chat)).thenReturn(chat);

        Chat actualChat = chatService.save(chat);

        assertEquals(actualChat, chat);
    }

    @Test
    public void findByIdTest() throws RestException {
        when(chatRepository.findById(chat.getId())).thenReturn(Optional.of(chat));

        Chat actualChat = chatService.findById(chat.getId());

        assertEquals(actualChat, chat);
    }

}
