package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.dao.UserDAO;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.Staff;

public class AssignComplaintFrame extends JFrame {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final UserDAO userDAO = new UserDAO();

    private JTable complaintsTable;
    private DefaultTableModel tableModel;
    private JComboBox<Staff> staffComboBox;

    public AssignComplaintFrame() {

        setTitle("Admin - Assign Complaints");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
                "Assign Complaints to Staff", SwingConstants.CENTER);
        heading.setFont(
                new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        mainPanel.add(heading, BorderLayout.NORTH);

        String[] columns = {
                "Complaint ID", "Student ID", "Title",
                "Category", "Priority", "Status", "Assigned Staff"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        complaintsTable = new JTable(tableModel);
        complaintsTable.setRowHeight(25);

        mainPanel.add(
                new JScrollPane(complaintsTable), BorderLayout.CENTER);

        staffComboBox = new JComboBox<>();

        JButton assignButton = new JButton("Assign Selected");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel bottomPanel = new JPanel(new FlowLayout());

        bottomPanel.add(new JLabel("Select Staff:"));
        bottomPanel.add(staffComboBox);
        bottomPanel.add(assignButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        assignButton.addActionListener(
                event -> assignSelectedComplaint());

        refreshButton.addActionListener(event -> loadComplaints());

        backButton.addActionListener(event -> dispose());

        add(mainPanel);

        loadStaff();
        loadComplaints();
    }

    private void loadStaff() {

        staffComboBox.removeAllItems();

        List<Staff> staffList = userDAO.getAllStaff();

        for (Staff staff : staffList) {
            staffComboBox.addItem(staff);
        }

        if (staffList.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No staff members found. Check your users table.");
        }
    }

    private void loadComplaints() {

        tableModel.setRowCount(0);

        List<Complaint> complaints = complaintDAO.getAllComplaints();

        for (Complaint complaint : complaints) {

            String studentId = complaint.getStudent() == null
                    ? "N/A"
                    : String.valueOf(
                            complaint.getStudent().getUserId());

            String category = complaint.getCategory() == null
                    ? "N/A"
                    : complaint.getCategory().getCategoryName();

            String staffName = complaint.getAssignedStaff() == null
                    ? "Not Assigned"
                    : complaint.getAssignedStaff().getFullName();

            Object[] row = {
                    complaint.getComplaintId(),
                    studentId,
                    complaint.getTitle(),
                    category,
                    complaint.getPriority(),
                    complaint.getStatus(),
                    staffName
            };

            tableModel.addRow(row);
        }
    }

    private void assignSelectedComplaint() {

        int selectedRow = complaintsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a complaint from the table.");
            return;
        }

        Staff selectedStaff =
                (Staff) staffComboBox.getSelectedItem();

        if (selectedStaff == null) {
            JOptionPane.showMessageDialog(
                    this, "Please select a staff member.");
            return;
        }

        int complaintId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString());

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Assign complaint #" + complaintId + " to "
                        + selectedStaff.getFullName() + "?",
                "Confirm Assignment",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = complaintDAO.assignComplaint(
                complaintId, selectedStaff.getUserId());

        if (success) {
            JOptionPane.showMessageDialog(
                    this, "Complaint assigned successfully!");
            loadComplaints();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Assignment failed. Please check the database.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}