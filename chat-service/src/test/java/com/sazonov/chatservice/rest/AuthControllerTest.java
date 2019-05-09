package com.sazonov.chatservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sazonov.chatservice.form.UserForm;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.security.provider.JwtTokenProvider;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider provider;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private UserForm userForm;
    private User user;
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

        userForm =  UserForm.builder()
                .login(user.getLogin())
                .name(user.getName())
                .password(user.getPassword())
                .passwordConfirm(user.getPassword())
                .build();
    }

    /*@Test
    public void signUpTest() throws Exception {
        when(userService.signUp(user)).thenReturn(user);

        mockMvc.perform(
                post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.login").value(user.getLogin()));
    }*/

    @Test
    public void signInTest() throws Exception {
        when(userService.login(user)).thenReturn(anyString());

        mockMvc.perform(
                post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userForm)))
                .andExpect(status().isOk());
    }

    @Test
    public void signInWithInCorrectValueTest() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/signin"))
                .andExpect(status().is4xxClientError());
    }
}
