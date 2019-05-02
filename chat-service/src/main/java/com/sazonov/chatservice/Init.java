package com.sazonov.chatservice;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Members;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.repository.ChatRepository;
import com.sazonov.chatservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList( "ROLE_USER"))
                .build()
        );
        this.users.save(User.builder()
                .login("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );
        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));

        Chat chat = Chat.builder()
                .name("My chat")
                .build();

        Members members = Members.builder()
                .user(users.getOne(1L))
                .chat(chat)
                .build();
        List<Members> members1 = new ArrayList<>();
        members1.add(members);


        chatRepository.save(chat);
    }
}
