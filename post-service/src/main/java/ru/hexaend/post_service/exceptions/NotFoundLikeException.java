package ru.hexaend.post_service.exceptions;

public class NotFoundLikeException extends RuntimeException {
    public NotFoundLikeException(String message) {
        super(message);
    }

    public NotFoundLikeException() {
        super("Like not found");
    }
}
