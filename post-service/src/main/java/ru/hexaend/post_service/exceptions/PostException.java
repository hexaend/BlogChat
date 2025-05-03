package ru.hexaend.post_service.exceptions;

public class PostException extends RuntimeException {
    public PostException(String message) {
        super(message);
    }
}
