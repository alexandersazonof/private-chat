package com.sazonov.chatservice.dto;

import com.sazonov.chatservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String login;
    private String password;

    public User from(){

        return User.builder()
                .login(login)
                .password(password)
                .build();
    }
}
