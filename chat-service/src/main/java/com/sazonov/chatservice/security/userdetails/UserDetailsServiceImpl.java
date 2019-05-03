package com.sazonov.chatservice.security.userdetails;

import com.sazonov.chatservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetails")
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserRepository users;

    public UserDetailsServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.users.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}
