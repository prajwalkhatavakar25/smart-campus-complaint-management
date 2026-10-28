package com.smartcampus.cms.service;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.dao.ComplaintUpdateDAO;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.model.Staff;
import com.smartcampus.cms.model.User;

public class ComplaintWorkflowService {

    private final ComplaintDAO complaintDAO;
    private final ComplaintUpdateDAO complaintUpdateDAO;

    public ComplaintWorkflowService() {
        this.complaintDAO = new ComplaintDAO();
        this.complaintUpdateDAO = new ComplaintUpdateDAO();
    }

    public boolean assignComplaint(int complaintId, Staff staff,
                                   User assignedBy) {

        Complaint complaint = complaintDAO.getComplaintById(complaintId);

        if (complaint == null) {
            System.out.println("Complaint was not found.");
            return false;
        }

        if (staff == null || assignedBy == null) {
            System.out.println(
                    "Staff and admin details are required."
            );
            return false;
        }

        if (complaint.getStatus()
                != ComplaintStatus.SUBMITTED) {

            System.out.println(
                    "Only a submitted complaint can be assigned."
            );
            return false;
        }

        if (complaintDAO.assignComplaint(
                complaintId, staff.getUserId())) {

            complaintUpdateDAO.createUpdate(
                    complaintId,
                    assignedBy.getUserId(),
                    ComplaintStatus.SUBMITTED,
                    ComplaintStatus.ASSIGNED,
                    "Complaint assigned to "
                            + staff.getFullName()
            );

            return true;
        }

        return false;
    }

    public boolean updateStatus(int complaintId,
                                ComplaintStatus newStatus,
                                User updatedBy,
                                String updateMessage) {

        Complaint complaint = complaintDAO.getComplaintById(complaintId);

        if (complaint == null) {
            System.out.println("Complaint was not found.");
            return false;
        }

        if (updatedBy == null || updateMessage == null
                || updateMessage.isBlank()) {

            System.out.println(
                    "Updater details and a message are required."
            );
            return false;
        }

        ComplaintStatus oldStatus = complaint.getStatus();

        if (!isValidStatusTransition(oldStatus, newStatus)) {
            System.out.println(
                    "Invalid status change: " + oldStatus
                            + " to " + newStatus
            );
            return false;
        }

        if (complaintDAO.updateComplaintStatus(
                complaintId, newStatus)) {

            complaintUpdateDAO.createUpdate(
                    complaintId,
                    updatedBy.getUserId(),
                    oldStatus,
                    newStatus,
                    updateMessage
            );

            return true;
        }

        return false;
    }

    private boolean isValidStatusTransition(
            ComplaintStatus oldStatus,
            ComplaintStatus newStatus) {

        return switch (oldStatus) {
            case SUBMITTED ->
                    newStatus == ComplaintStatus.ASSIGNED;

            case ASSIGNED ->
                    newStatus == ComplaintStatus.IN_PROGRESS;

            case IN_PROGRESS ->
                    newStatus == ComplaintStatus.RESOLVED;

            case RESOLVED ->
                    newStatus == ComplaintStatus.CLOSED
                            || newStatus == ComplaintStatus.IN_PROGRESS;

            case CLOSED -> false;
        };
    }
}