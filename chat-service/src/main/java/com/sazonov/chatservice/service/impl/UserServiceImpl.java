package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.repository.UserRepository;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.security.provider.JwtTokenProvider;
import com.sazonov.chatservice.service.UserService;
import com.sazonov.chatservice.service.exception.UserExistsException;
import com.sazonov.chatservice.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User signUp(User user) throws RestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserExistsException("User exits");
        }

        return userRepository.save(user);
    }

    @Override
    public String login(User user) throws RestException {

        try {

            String login = user.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, user.getPassword()));
            String token = jwtTokenProvider.createToken(user, userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Username " + login + "not found")).getRoles());

            return token;

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @Override
    public User findById(Long id) throws RestException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User findByLogin(String login) throws RestException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() throws RestException {
        return userRepository.findAll();
    }
}
