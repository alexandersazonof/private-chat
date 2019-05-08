package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.UserForm;
import com.sazonov.chatservice.model.ResponseMessage;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {


    @Autowired
    private UserService userService;


    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody User user) throws RestException {
        log.info("Sign in action");

        String token = userService.login(user);

        return ok(
                ResponseMessage.builder()
                .put("success", "true")
                .put("token", token)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid UserForm userForm) throws RestException {
        log.info("Sign up action");

        User user = userForm.from();
        user = userService.signUp(user);

        return ok(
                ResponseMessage.builder()
                .put("success", "true")
                .put("login", user.getLogin())
        );
    }
}
