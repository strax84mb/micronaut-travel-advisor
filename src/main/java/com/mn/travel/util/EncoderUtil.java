package com.mn.travel.util;

import java.security.NoSuchAlgorithmException;

public interface EncoderUtil {

    String encode(String text, byte[] salt) throws NoSuchAlgorithmException;

    byte[] generateSalt();
}
