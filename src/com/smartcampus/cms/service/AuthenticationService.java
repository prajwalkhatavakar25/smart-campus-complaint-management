package com.smartcampus.cms.service;

import com.smartcampus.cms.dao.UserDAO;
import com.smartcampus.cms.exception.AuthenticationException;
import com.smartcampus.cms.model.User;
import com.smartcampus.cms.util.PasswordUtil;

public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String email, String plainPassword)
            throws AuthenticationException {

        if (email == null || email.isBlank()
                || plainPassword == null || plainPassword.isBlank()) {

            throw new AuthenticationException(
                    "Email and password cannot be empty."
            );
        }

        User user = userDAO.getUserByEmail(email.trim());

        if (user == null) {
            throw new AuthenticationException(
                    "No user is registered with this email."
            );
        }

        String enteredPasswordHash =
                PasswordUtil.hashPassword(plainPassword);

        if (!enteredPasswordHash.equals(user.getPasswordHash())) {
            throw new AuthenticationException("Incorrect password.");
        }

        return user;
    }
}