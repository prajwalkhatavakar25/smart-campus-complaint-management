package com.smartcampus.cms.model;

public abstract class User {
	 private int userId;
	    private String fullName;
	    private String email;
	    private String passwordHash;
	    private UserRole role;
	    private String department;

	    public User() {
	    }

	    public User(int userId, String fullName, String email,
	                String passwordHash, UserRole role, String department) {
	        this.userId = userId;
	        this.fullName = fullName;
	        this.email = email;
	        this.passwordHash = passwordHash;
	        this.role = role;
	        this.department = department;
	    }

	    public int getUserId() {
	        return userId;
	    }

	    public void setUserId(int userId) {
	        this.userId = userId;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPasswordHash() {
	        return passwordHash;
	    }

	    public void setPasswordHash(String passwordHash) {
	        this.passwordHash = passwordHash;
	    }

	    public UserRole getRole() {
	        return role;
	    }

	    public void setRole(UserRole role) {
	        this.role = role;
	    }

	    public String getDepartment() {
	        return department;
	    }

	    public void setDepartment(String department) {
	        this.department = department;
	    }

	    public abstract String getRoleDescription();

}
