package com.sazonov.chatservice.form;

import com.sazonov.chatservice.form.exception.PasswordNotMatchException;
import com.sazonov.chatservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;
    private String name;

    public User from() throws PasswordNotMatchException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        if (!password.equals(passwordConfirm)) {
            throw new PasswordNotMatchException("Passwords do not match");
        }

        return User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .roles(Arrays.asList("ROLE_USER"))
                .build();
    }
}
