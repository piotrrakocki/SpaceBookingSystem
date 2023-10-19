package com.myproject.spacebooking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RocketNotFoundException extends RuntimeException {

    public RocketNotFoundException(String message) {
        super(message);
    }
}
