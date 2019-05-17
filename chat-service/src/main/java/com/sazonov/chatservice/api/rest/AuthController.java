package com.sazonov.chatservice.api.rest;

import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.domain.ApiMessage;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.dto.UserDto;
import com.sazonov.chatservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@Api(value = "Authentication management system", description = "Operation for sign in and sign up users")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/signin")
    @ApiOperation(value = "Sign in user, and response token")
    public ResponseEntity signIn(@RequestBody User user) throws RestException {
        log.info("Sign in action");

        String token = userService.login(user);

        User userResponse = userService.findByLogin(user.getLogin());

        return ok(
                ApiMessage.builder()
                .put("success", "true")
                .put("token", token)
                .put("id", userResponse.getId())
        );
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Create user")
    public ResponseEntity signUp(@RequestBody @Valid UserDto userDto) throws RestException {
        log.info("Sign up action");

        User user = userDto.from();
        user = userService.signUp(user);

        return ok(
                ApiMessage.builder()
                .put("success", "true")
                .put("login", user.getLogin())
        );
    }
}
