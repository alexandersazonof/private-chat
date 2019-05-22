package com.sazonov.chatservice.api.rest;

import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.domain.ApiMessage;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.security.util.SecurityUtil;
import com.sazonov.chatservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@Api(value = "User management system")
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @Autowired
    public UserController(UserService userService, SecurityUtil securityUtil) {
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get list of users")
    public ResponseEntity getAllUsers() throws RestException {

        User mainUser = securityUtil.getUserFromSecurityContext();


        List<User> users = userService.findAll().stream().filter(user -> user.getId() != mainUser.getId()).collect(Collectors.toList());

        users.forEach(user -> securityUtil.deletePassword(user));

        return ok(
                ApiMessage.builder()
                .put(MessageResponse.RESPONSE_SUCCESS, true)
                .put("users", users)
        );
    }
}
