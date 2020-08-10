package com.mn.travel.exceptions;

public class CommentDoesNotExistException extends RuntimeException {

    public CommentDoesNotExistException(Long id) {
        super(String.format("Comment with ID %d does not exist!", id));
    }
}
