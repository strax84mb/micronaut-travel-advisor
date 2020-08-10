package com.mn.travel.services;

import java.security.NoSuchAlgorithmException;

public interface SignupService {

    Long signup(String username, String password) throws NoSuchAlgorithmException;
}
