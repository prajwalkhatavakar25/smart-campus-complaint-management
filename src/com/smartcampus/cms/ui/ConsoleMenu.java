package com.smartcampus.cms.ui;

import java.util.List;
import java.util.Scanner;

import com.smartcampus.cms.dao.CategoryDAO;
import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.dao.UserDAO;
import com.smartcampus.cms.exception.AuthenticationException;
import com.smartcampus.cms.model.Admin;
import com.smartcampus.cms.model.Category;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.model.Priority;
import com.smartcampus.cms.model.Staff;
import com.smartcampus.cms.model.Student;
import com.smartcampus.cms.model.User;
import com.smartcampus.cms.service.AuthenticationService;
import com.smartcampus.cms.service.ComplaintWorkflowService;

import com.smartcampus.cms.dao.ComplaintUpdateDAO;
import com.smartcampus.cms.service.ReportGenerator;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final AuthenticationService authenticationService =
            new AuthenticationService();

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final UserDAO userDAO = new UserDAO();

    private final ComplaintWorkflowService workflowService =
            new ComplaintWorkflowService();
    
    private final ComplaintUpdateDAO complaintUpdateDAO =
            new ComplaintUpdateDAO();

    public void start() {

        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println(" SMART CAMPUS COMPLAINT MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Login");
            System.out.println("0. Exit");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> login();
                case 0 -> {
                    running = false;
                    System.out.println("Thank you for using the system.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void login() {

        String email = readText("Email: ");
        String password = readText("Password: ");

        try {
            User user = authenticationService.login(email, password);

            System.out.println("\nLogin successful. Welcome, "
                    + user.getFullName() + ".");

            switch (user.getRole()) {
                case STUDENT -> studentMenu((Student) user);
                case STAFF -> staffMenu((Staff) user);
                case ADMIN -> adminMenu((Admin) user);
            }

        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void studentMenu(Student student) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Submit a complaint");
            System.out.println("2. View my complaints");
            System.out.println("3. Delete a submitted complaint");
            System.out.println("4. View complaint history");
            System.out.println("0. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> submitComplaint(student);
                case 2 -> viewStudentComplaints(student);
                case 3 -> deleteStudentComplaint(student);
                case 4 -> viewComplaintHistory();
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void staffMenu(Staff staff) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("1. View assigned complaints");
            System.out.println("2. Update complaint status");
            System.out.println("3. View complaint history");
            System.out.println("0. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> viewStaffComplaints(staff);
                case 2 -> updateComplaintStatus(staff);
                case 3 -> viewComplaintHistory();
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void adminMenu(Admin admin) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View all complaints");
            System.out.println("2. Assign complaint to staff");
            System.out.println("3. View complaint history");
            System.out.println("4. Generate report in background");
            System.out.println("0. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> viewAllComplaints();
                case 2 -> assignComplaint(admin);
                case 3 -> viewComplaintHistory();
                case 4 -> generateReportInBackground();
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void submitComplaint(Student student) {

        System.out.println("\n--- Available Categories ---");

        for (Category category : categoryDAO.getAllCategories()) {
            System.out.println(category.getCategoryId()
                    + ". " + category.getCategoryName());
        }

        int categoryId = readInt("Enter category ID: ");

        Category selectedCategory = findCategoryById(categoryId);

        if (selectedCategory == null) {
            System.out.println("Invalid category ID.");
            return;
        }

        Complaint complaint = new Complaint();

        complaint.setStudent(student);
        complaint.setCategory(selectedCategory);
        complaint.setTitle(readText("Complaint title: "));
        complaint.setDescription(readText("Description: "));
        complaint.setLocation(readText("Location: "));
        complaint.setPriority(readPriority());
        complaint.setStatus(ComplaintStatus.SUBMITTED);

        if (complaintDAO.createComplaint(complaint)) {
            System.out.println("Complaint submitted successfully.");
            System.out.println("Complaint ID: "
                    + complaint.getComplaintId());
        }
    }

    private void viewStudentComplaints(Student student) {

        List<Complaint> complaints =
                complaintDAO.getComplaintsByStudentId(
                        student.getUserId()
                );

        printComplaintList(complaints);
    }

    private void deleteStudentComplaint(Student student) {

        int complaintId = readInt(
                "Enter complaint ID to delete: "
        );

        Complaint complaint =
                complaintDAO.getComplaintById(complaintId);

        if (complaint == null
                || complaint.getStudent().getUserId()
                != student.getUserId()) {

            System.out.println(
                    "You cannot delete this complaint."
            );
            return;
        }

        if (complaint.getStatus()
                != ComplaintStatus.SUBMITTED) {

            System.out.println(
                    "Only submitted complaints can be deleted."
            );
            return;
        }

        String confirmation = readText(
                "Type YES to confirm deletion: "
        );

        if (!confirmation.equalsIgnoreCase("YES")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        if (complaintDAO.deleteComplaint(complaintId)) {
            System.out.println("Complaint deleted successfully.");
        }
    }

    private void viewStaffComplaints(Staff staff) {

        List<Complaint> complaints =
                complaintDAO.getComplaintsByStaffId(
                        staff.getUserId()
                );

        printComplaintList(complaints);
    }

    private void updateComplaintStatus(Staff staff) {

        int complaintId = readInt(
                "Enter complaint ID to update: "
        );

        Complaint complaint =
                complaintDAO.getComplaintById(complaintId);

        if (complaint == null
                || complaint.getAssignedStaff() == null
                || complaint.getAssignedStaff().getUserId()
                != staff.getUserId()) {

            System.out.println(
                    "This complaint is not assigned to you."
            );
            return;
        }

        ComplaintStatus newStatus = readStaffStatus();

        String message = readText(
                "Update message: "
        );

        if (workflowService.updateStatus(
                complaintId, newStatus, staff, message)) {

            System.out.println("Complaint status updated.");
        }
    }

    private void viewAllComplaints() {

        List<Complaint> complaints =
                complaintDAO.getAllComplaints();

        printComplaintList(complaints);
    }

    private void assignComplaint(Admin admin) {

        int complaintId = readInt(
                "Enter complaint ID to assign: "
        );

        Complaint complaint =
                complaintDAO.getComplaintById(complaintId);

        if (complaint == null) {
            System.out.println("Complaint was not found.");
            return;
        }

        String staffEmail = readText(
                "Enter staff email: "
        );

        User staffUser =
                userDAO.getUserByEmail(staffEmail);

        if (!(staffUser instanceof Staff staff)) {
            System.out.println(
                    "No staff member was found with this email."
            );
            return;
        }

        if (workflowService.assignComplaint(
                complaintId, staff, admin)) {

            System.out.println(
                    "Complaint assigned to "
                            + staff.getFullName() + "."
            );
        }
    }
    private void viewComplaintHistory() {

        int complaintId = readInt(
                "Enter complaint ID to view history: "
        );

        List<String> history =
                complaintUpdateDAO
                        .getUpdateHistoryByComplaintId(
                                complaintId
                        );

        if (history.isEmpty()) {
            System.out.println(
                    "No history was found for this complaint."
            );
            return;
        }

        System.out.println("\n--- Complaint History ---");

        for (String record : history) {
            System.out.println(record);
        }
    }
    private void generateReportInBackground() {

        Thread reportThread = new Thread(
                new ReportGenerator(),
                "ComplaintReportGenerator"
        );

        reportThread.start();

        System.out.println(
                "Report generation started in the background."
        );
    }

    private Category findCategoryById(int categoryId) {

        for (Category category : categoryDAO.getAllCategories()) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }

        return null;
    }

    private Priority readPriority() {

        while (true) {
            String input = readText(
                    "Priority (LOW, MEDIUM, HIGH): "
            ).toUpperCase();

            try {
                return Priority.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Enter LOW, MEDIUM, or HIGH."
                );
            }
        }
    }

    private ComplaintStatus readStaffStatus() {

        while (true) {
            System.out.println("1. IN_PROGRESS");
            System.out.println("2. RESOLVED");
            System.out.println("3. CLOSED");

            int choice = readInt("Choose new status: ");

            switch (choice) {
                case 1 -> {
                    return ComplaintStatus.IN_PROGRESS;
                }
                case 2 -> {
                    return ComplaintStatus.RESOLVED;
                }
                case 3 -> {
                    return ComplaintStatus.CLOSED;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printComplaintList(
            List<Complaint> complaints) {

        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }

        for (Complaint complaint : complaints) {
            System.out.println("\n----------------------------------------");
            System.out.println(complaint);
            System.out.println("Category: "
                    + complaint.getCategory().getCategoryName());
            System.out.println("Location: "
                    + complaint.getLocation());
            System.out.println("Submitted: "
                    + complaint.getSubmittedAt());

            if (complaint.getAssignedStaff() != null) {
                System.out.println("Assigned Staff: "
                        + complaint.getAssignedStaff()
                                .getFullName());
            }
        }

        System.out.println("\n----------------------------------------");
    }

    private int readInt(String prompt) {

        while (true) {
            String input = readText(prompt);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private String readText(String prompt) {

        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}