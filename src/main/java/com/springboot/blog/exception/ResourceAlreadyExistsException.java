package com.springboot.blog.exception;
import org.springframework.http.HttpStatus;
public class ResourceAlreadyExistsException extends RuntimeException {
    private HttpStatus status;    public ResourceAlreadyExistsException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }}