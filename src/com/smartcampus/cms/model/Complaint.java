package com.smartcampus.cms.model;

import java.time.LocalDateTime;

public class Complaint {
	
	private int complaintId;
    private Student student;
    private Category category;
    private Staff assignedStaff;

    private String title;
    private String description;
    private String location;

    private Priority priority;
    private ComplaintStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    public Complaint() {
        this.priority = Priority.MEDIUM;
        this.status = ComplaintStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Complaint(int complaintId, Student student, Category category,
                     Staff assignedStaff, String title, String description,
                     String location, Priority priority,
                     ComplaintStatus status, LocalDateTime submittedAt,
                     LocalDateTime updatedAt) {

        this.complaintId = complaintId;
        this.student = student;
        this.category = category;
        this.assignedStaff = assignedStaff;
        this.title = title;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.status = status;
        this.submittedAt = submittedAt;
        this.updatedAt = updatedAt;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Staff getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(Staff assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Complaint ID: " + complaintId
                + " | Title: " + title
                + " | Status: " + status
                + " | Priority: " + priority;
    }

}
