package com.sazonov.chatservice.service;

import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.repository.UserRepository;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @Before
    public void init(){
        user = User.builder()
                .id(1L)
                .login("login1")
                .password("password")
                .name("maga")
                .roles(Arrays.asList("ROLE_USER"))
                .build();
    }

    @Test
    public void findUserByLoginWithCorrectValue() throws RestException {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        User tempUser = userService.findByLogin(user.getLogin());

        assertEquals(tempUser, user);
    }

    @Test
    public void findByIdWithCorrectValue() throws RestException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User tempUser = userService.findById(user.getId());

        assertEquals(tempUser, user);
    }



}
