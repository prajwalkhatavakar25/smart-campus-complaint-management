package com.smartcampus.cms.model;

public class Student extends User {

    public Student() {
        setRole(UserRole.STUDENT);
    }

    public Student(int userId, String fullName, String email,
                   String passwordHash, String department) {

        super(userId, fullName, email, passwordHash,
                UserRole.STUDENT, department);
    }

    @Override
    public String getRoleDescription() {
        return "Student: can submit and track complaints.";
    }
}
