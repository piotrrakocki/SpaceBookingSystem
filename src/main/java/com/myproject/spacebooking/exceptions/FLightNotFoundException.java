package com.myproject.spacebooking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FLightNotFoundException extends RuntimeException {

    public FLightNotFoundException(String message) {
        super(message);
    }
}
