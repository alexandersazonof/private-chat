package com.sazonov.chatservice.security.util;

import com.sazonov.chatservice.model.Chat;
import com.sazonov.chatservice.model.Message;
import com.sazonov.chatservice.model.User;
import com.sazonov.chatservice.rest.exception.RestException;
import com.sazonov.chatservice.security.exception.AccessDeniedException;
import com.sazonov.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityUtil {

    @Autowired
    private UserService userService;

    public boolean userExistsInChats(User user, List<Chat> chats) throws AccessDeniedException {

        int countChats = 0;

        for (Chat chat :chats) {

            try {

                userExistInChat(user, chat);

            } catch (AccessDeniedException e) {
                countChats++;
            }

        }

        if (countChats == 0) {
            throw new AccessDeniedException("Access denied");
        }

        return true;
    }

    public boolean userExistInChat(User user, Chat chat) throws AccessDeniedException {

        int countChats = 0;

        for (User u :chat.getUsers()) {

            if (u.getLogin().equals(user.getLogin())) {
                countChats++;
            }
        }

        if (countChats == 0) {
            throw new AccessDeniedException("Access denied");
        }

        return true;
    }

    public User getUserFromSecurityContext() throws RestException {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.findByLogin(login);
    }



    public boolean userHasAccess(Long id) throws RestException {

        if (id != getUserFromSecurityContext().getId()) {
            throw new AccessDeniedException("Access denied");
        }

        return true;
    }

    public boolean userHasAccessToMessage(User user, Message message) throws AccessDeniedException {

        if(user.getId() != message.getUser().getId()) {
            throw new AccessDeniedException("Access denied");
        }

        return true;
    }
}
