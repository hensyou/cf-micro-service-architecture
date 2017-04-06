package com.cloudnativecoffee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderDeletionException extends RuntimeException {

    private static final long serialVersionUID = -2678155065724559391L;

    public OrderDeletionException(String message) {
        super(message);
    }

    public OrderDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
