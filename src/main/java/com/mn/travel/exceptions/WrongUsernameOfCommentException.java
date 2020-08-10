package com.mn.travel.exceptions;

public class WrongUsernameOfCommentException extends RuntimeException {

    public WrongUsernameOfCommentException(String username) {
        super(String.format("Only administrators can change or delete comments of other users. " +
                "User %s can only change or delete his/her own comments", username));
    }
}
