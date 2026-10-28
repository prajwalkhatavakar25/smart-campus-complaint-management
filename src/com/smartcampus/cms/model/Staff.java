package com.smartcampus.cms.model;

public class Staff extends User {

    public Staff() {
        setRole(UserRole.STAFF);
    }

    public Staff(int userId, String fullName, String email,
                 String passwordHash, String department) {

        super(userId, fullName, email, passwordHash,
                UserRole.STAFF, department);
    }

    @Override
    public String getRoleDescription() {
        return "Staff: can work on assigned complaints.";
    }
}