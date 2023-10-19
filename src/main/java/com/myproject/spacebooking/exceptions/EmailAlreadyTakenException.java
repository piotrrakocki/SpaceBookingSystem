package com.myproject.spacebooking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
