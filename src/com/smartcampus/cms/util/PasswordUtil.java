package com.smartcampus.cms.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hashedPassword = new StringBuilder();

            for (byte hashByte : hashBytes) {
                hashedPassword.append(
                        String.format("%02x", hashByte)
                );
            }

            return hashedPassword.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to hash password.", e);
        }
    }
}