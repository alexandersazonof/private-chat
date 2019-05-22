package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.api.rest.util.MessageResponse;
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

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(User user) throws RestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserExistsException("User exits");
        }

        return userRepository.save(user);
    }

    @Override
    public String login(User user) {

        try {

            String login = user.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, user.getPassword()));
            return jwtTokenProvider.createToken(user, userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Username " + login + "not found")).getRoles());

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @Override
    public User findById(Long id) throws RestException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageResponse.USER_NOT_FOUND));
    }

    @Override
    public User findByLogin(String login) throws RestException {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(MessageResponse.USER_NOT_FOUND));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
