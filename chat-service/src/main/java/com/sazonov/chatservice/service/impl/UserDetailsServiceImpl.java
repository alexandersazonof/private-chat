package com.sazonov.chatservice.service.impl;

import com.sazonov.chatservice.domain.CurrentUser;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login: " + username + " not found"));

        return new CurrentUser(user);
    }
}
