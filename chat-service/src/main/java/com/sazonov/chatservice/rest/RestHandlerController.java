package com.sazonov.chatservice.rest;

import com.sazonov.chatservice.form.exception.PasswordNotMatchException;
import com.sazonov.chatservice.service.exception.UserExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class RestHandlerController {

    @ExceptionHandler({PasswordNotMatchException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public HashMap incorrectArguments(Exception exception) {
        log.warn(exception.getMessage());

        return message("Invalid arguments", HttpStatus.UNPROCESSABLE_ENTITY);
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



    private HashMap message(String message, HttpStatus code) {
        HashMap model = new HashMap();

        model.put("message", message);
        model.put("code", code.value());

        return model;
    }
}
