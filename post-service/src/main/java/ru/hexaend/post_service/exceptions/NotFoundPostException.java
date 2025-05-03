package ru.hexaend.post_service.exceptions;

public class NotFoundPostException extends RuntimeException {
    public NotFoundPostException(String message) {
        super(message);
    }

    public NotFoundPostException() {
        super("Post not found");
    }
}
