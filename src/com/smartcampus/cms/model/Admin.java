package com.smartcampus.cms.model;

public class Admin extends User {

    public Admin() {
        setRole(UserRole.ADMIN);
    }

    public Admin(int userId, String fullName, String email,
                 String passwordHash, String department) {

        super(userId, fullName, email, passwordHash,
                UserRole.ADMIN, department);
    }

    @Override
    public String getRoleDescription() {
        return "Admin: can manage users, categories, and complaint assignments.";
    }
}