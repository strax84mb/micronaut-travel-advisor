package com.mn.travel.exceptions;

public class IncorrectCredentialsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Provided credentials are incorrect!";
    }
}
