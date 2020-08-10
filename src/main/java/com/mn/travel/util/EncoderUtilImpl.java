package com.mn.travel.util;

import javax.inject.Singleton;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Singleton
public class EncoderUtilImpl implements EncoderUtil {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    public String encode(String text, byte[] salt) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        var encoded = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(encoded);
    }

    @Override
    public byte[] generateSalt() {
        var bytes = new byte[16];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
