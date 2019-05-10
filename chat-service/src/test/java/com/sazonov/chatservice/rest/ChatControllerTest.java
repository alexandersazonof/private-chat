package com.sazonov.chatservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sazonov.chatservice.form.ChatForm;
import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.security.provider.JwtTokenProvider;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.impl.ChatServiceImpl;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    private MockMvc mockMvc;


    @InjectMocks
    private ChatController chatController;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ChatServiceImpl chatService;

    @MockBean
    private SecurityUtil securityUtil;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user;
    private ChatForm chatForm;
    private Chat chat;
    private List<Chat> chats;
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

        chatForm = ChatForm.builder()
                .name("mychat")
                .members(Arrays.asList(Long.valueOf(1), 1L))
        .build();

        chat = Chat.builder()
                .id(1L)
                .name(chatForm.getName())
                .users(Arrays.asList(user))
                .build();

        chats = Arrays.asList(chat);
    }

    @Test
    public void getAllByUserIdWithCorrectValue() throws Exception {
        when(chatService.findByUserId(user.getId())).thenReturn(chats);

        mockMvc.perform(
                get("/api/v1/chats")
                .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.chats[0].name").value(chat.getName()))
                .andExpect(jsonPath("response.chats[0].id").value(chat.getId()));
    }

    @Test
    public void getAllByUserIdWithoutUserId() throws Exception {
        mockMvc.perform(
                get("/api/v1/chats"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllByUserIdWithIncorrectValue() throws Exception {
        mockMvc.perform(
                get("/api/v1/chats")
                .param("userId", "asd"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Incorrect value"));
    }

    @Test
    public void createChatWithCorrectValue()throws Exception{
        when(userService.findById(user.getId())).thenReturn(user);

        mockMvc.perform(
                post("/api/v1/chats")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(chatForm)))
                .andExpect(status().isOk());
    }

    @Test
    public void createChatWithoutContent() throws Exception {

        mockMvc.perform(
                post("/api/v1/chats"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getChatByIdWithCorrectId() throws Exception{
        when( chatService.findById(chat.getId())).thenReturn(chat);

        mockMvc.perform(
                get(String.format("/api/v1/chats/%d", chat.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.chat.id").value(chat.getId()))
                .andExpect(jsonPath("response.chat.name").value(chat.getName()));
    }

    @Test
    public void getChatByIdWithoutId() throws Exception{
        mockMvc.perform(
                get(String.format("/api/v1/chats/%s", "asd")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteChatByIdWithCorrectId() throws Exception{

        mockMvc.perform(
                delete(String.format("/api/v1/chats/%d", chat.getId())))
                .andExpect(status().isOk());
    }


}
