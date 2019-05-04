package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.exception.PasswordNotMatchException;
import com.sazonov.chatservice.security.exception.AccessDeniedException;
import com.sazonov.chatservice.service.exception.ChatNotFoundException;
import com.sazonov.chatservice.service.exception.UserExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class RestHandlerController {

    @ExceptionHandler({PasswordNotMatchException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public HashMap incorrectArguments(Exception exception) {
        log.warn(exception.getMessage());

        return message("Invalid arguments", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap badRequest(Exception e) {
        log.warn(e.getMessage());

        return message("Incorrect value", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap userExists(Exception e) {
        log.warn(e.getMessage());

        return message("User exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap chatNotFound(Exception e) {
        log.warn(e.getMessage());

        return message("Chat not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap missingParameters(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());

        return message("Missing parameters: " + e.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HashMap accessDenied(Exception e) {
        log.warn(e.getMessage());

        return message("Access denied", HttpStatus.FORBIDDEN);
    }




    private HashMap message(String message, HttpStatus code) {
        HashMap model = new HashMap();

        model.put("message", message);
        model.put("code", code.value());

        return model;
    }
}
