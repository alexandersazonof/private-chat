package com.sazonov.chatservice;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.repository.ChatRepository;
import com.sazonov.chatservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@Slf4j
public class Init implements CommandLineRunner {


    @Autowired
    UserRepository users;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ChatRepository chatRepository;


    @Override
    public void run(String... args) throws Exception {
        //...
        this.users.save(User.builder()
                .login("user")
                .name("maga")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList( "ROLE_USER"))
                .build()
        );
        this.users.save(User.builder()
                .login("admin")
                .name("ara")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );
        log.debug("printing all users...");

        Chat chat = Chat.builder()
                .name("My chat")
                .build();


        chatRepository.save(chat);
    }
}
