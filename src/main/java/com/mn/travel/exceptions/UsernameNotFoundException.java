package com.mn.travel.exceptions;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String username) {
        super(String.format("User %s not found!", username));
    }
}
