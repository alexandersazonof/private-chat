package com.sazonov.chatservice.security.util;

import com.sazonov.chatservice.api.rest.util.MessageResponse;
import com.sazonov.chatservice.domain.Chat;
import com.sazonov.chatservice.domain.Message;
import com.sazonov.chatservice.domain.User;
import com.sazonov.chatservice.api.rest.exception.RestException;
import com.sazonov.chatservice.security.exception.AccessDeniedException;
import com.sazonov.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityUtil {

    private final UserService userService;

    @Autowired
    public SecurityUtil(UserService userService) {
        this.userService = userService;
    }

    public void userExistInChat(User user, Chat chat) throws AccessDeniedException {

        int countChats = 0;

        for (User u :chat.getUsers()) {
            if (u.getLogin().equals(user.getLogin())) {
                countChats++;
            }
        }

        if (countChats == 0) {
            throw new AccessDeniedException(MessageResponse.RESPONSE_SUCCESS);
        }
    }

    public User getUserFromSecurityContext() throws RestException {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.findByLogin(login);
    }



    public void userHasAccess(Long id) throws RestException {
        if (id != getUserFromSecurityContext().getId()) {
            throw new AccessDeniedException(MessageResponse.RESPONSE_SUCCESS);
        }
    }

    public void userHasAccessToMessage(User user, Message message) throws AccessDeniedException {
        if(user.getId() != message.getUser().getId()) {
            throw new AccessDeniedException(MessageResponse.RESPONSE_SUCCESS);
        }
    }

    public void deletePassword(User user) {
        if (user.getPassword() != null) {
            user.setPassword("");
        }
    }

    public void deletePassword(List<User> users) {
        users.forEach(user -> deletePassword(user));
    }

    public Chat deletePassword(Chat chat) {
        chat.getUsers().forEach(user -> deletePassword(user));
        return chat;
    }
}
