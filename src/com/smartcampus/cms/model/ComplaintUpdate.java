package com.smartcampus.cms.model;

import java.time.LocalDateTime;

public class ComplaintUpdate {
	 private int updateId;
	    private Complaint complaint;
	    private User updatedBy;

	    private ComplaintStatus oldStatus;
	    private ComplaintStatus newStatus;

	    private String updateMessage;
	    private LocalDateTime updatedAt;

	    public ComplaintUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }

	    public ComplaintUpdate(int updateId, Complaint complaint, User updatedBy,
	                           ComplaintStatus oldStatus,
	                           ComplaintStatus newStatus,
	                           String updateMessage,
	                           LocalDateTime updatedAt) {

	        this.updateId = updateId;
	        this.complaint = complaint;
	        this.updatedBy = updatedBy;
	        this.oldStatus = oldStatus;
	        this.newStatus = newStatus;
	        this.updateMessage = updateMessage;
	        this.updatedAt = updatedAt;
	    }

	    public int getUpdateId() {
	        return updateId;
	    }

	    public void setUpdateId(int updateId) {
	        this.updateId = updateId;
	    }

	    public Complaint getComplaint() {
	        return complaint;
	    }

	    public void setComplaint(Complaint complaint) {
	        this.complaint = complaint;
	    }

	    public User getUpdatedBy() {
	        return updatedBy;
	    }

	    public void setUpdatedBy(User updatedBy) {
	        this.updatedBy = updatedBy;
	    }

	    public ComplaintStatus getOldStatus() {
	        return oldStatus;
	    }

	    public void setOldStatus(ComplaintStatus oldStatus) {
	        this.oldStatus = oldStatus;
	    }

	    public ComplaintStatus getNewStatus() {
	        return newStatus;
	    }

	    public void setNewStatus(ComplaintStatus newStatus) {
	        this.newStatus = newStatus;
	    }

	    public String getUpdateMessage() {
	        return updateMessage;
	    }

	    public void setUpdateMessage(String updateMessage) {
	        this.updateMessage = updateMessage;
	    }

	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }

	    public void setUpdatedAt(LocalDateTime updatedAt) {
	        this.updatedAt = updatedAt;
	    }

}
