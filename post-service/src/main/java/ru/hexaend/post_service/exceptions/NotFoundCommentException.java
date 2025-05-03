package ru.hexaend.post_service.exceptions;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException(String message) {
        super(message);
    }

    public NotFoundCommentException() {
        super("Comment not found");
    }
}
