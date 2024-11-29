package com.springboot.blog.exception;

import lombok.Getter;

@Getter
public class InvalidImageException extends RuntimeException {
    private final String errorCode;

    public InvalidImageException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
