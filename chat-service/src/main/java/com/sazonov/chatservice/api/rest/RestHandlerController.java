package com.sazonov.chatservice.api.rest;

import com.sazonov.chatservice.dto.exception.PasswordNotMatchException;
import com.sazonov.chatservice.security.exception.AccessDeniedException;
import com.sazonov.chatservice.service.exception.ChatNotFoundException;
import com.sazonov.chatservice.service.exception.MessageNotFoundException;
import com.sazonov.chatservice.service.exception.UserExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RestHandlerController {


    @ExceptionHandler({PasswordNotMatchException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map incorrectArguments(Exception exception) {
        log.warn(exception.getMessage());

        return message("Invalid arguments");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map badRequest(Exception e) {
        log.warn(e.getMessage());

        return message("Incorrect value");
    }

    @ExceptionHandler({UserExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map userExists(Exception e) {
        log.warn(e.getMessage());

        return message("User exists");
    }

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map chatNotFound(Exception e) {
        log.warn(e.getMessage());

        return message("Chat not found");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map missingParameters(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());

        return message("Missing parameters: " + e.getParameterName());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map accessDenied(Exception e) {
        log.warn(e.getMessage());

        return message("Access denied");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map messageNotFound(Exception e) {
        log.warn(e.getMessage());

        return message("Message not found");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map incorrectConvertFromJson(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());

        return message("Incorrect arguments");
    }



    private Map message(String message) {
        Map model = new HashMap();

        model.put("message", message);

        return model;
    }
}
