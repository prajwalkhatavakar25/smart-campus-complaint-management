package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.model.Complaint;

public class AdminComplaintsFrame extends JFrame {

    private JTable complaintsTable;
    private DefaultTableModel tableModel;
    private final ComplaintDAO complaintDAO;

    public AdminComplaintsFrame() {

        complaintDAO = new ComplaintDAO();

        setTitle("Admin - All Complaints");
        setSize(1100, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
                "All Student Complaints", SwingConstants.CENTER);
        heading.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        mainPanel.add(heading, BorderLayout.NORTH);

        String[] columns = {
                "ID", "Student ID", "Category", "Title",
                "Location", "Priority", "Assigned Staff", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        complaintsTable = new JTable(tableModel);
        complaintsTable.setRowHeight(25);
        complaintsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] widths = {50, 90, 130, 200, 140, 90, 160, 120};

        for (int i = 0; i < widths.length; i++) {
            complaintsTable.getColumnModel()
                    .getColumn(i).setPreferredWidth(widths[i]);
        }

        mainPanel.add(
                new JScrollPane(complaintsTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(event -> loadComplaints());

        backButton.addActionListener(event -> dispose());

        add(mainPanel);

        loadComplaints();
    }

    private void loadComplaints() {

        tableModel.setRowCount(0);

        List<Complaint> complaints = complaintDAO.getAllComplaints();

        for (Complaint complaint : complaints) {

            String studentId = complaint.getStudent() == null
                    ? "N/A"
                    : String.valueOf(complaint.getStudent().getUserId());

            String category = complaint.getCategory() == null
                    ? "N/A"
                    : complaint.getCategory().getCategoryName();

            String staffName = complaint.getAssignedStaff() == null
                    ? "Not Assigned"
                    : complaint.getAssignedStaff().getFullName();

            Object[] row = {
                    complaint.getComplaintId(),
                    studentId,
                    category,
                    complaint.getTitle(),
                    complaint.getLocation(),
                    complaint.getPriority(),
                    staffName,
                    complaint.getStatus()
            };

            tableModel.addRow(row);
        }

        if (complaints.isEmpty()) {
            System.out.println("No complaints found, or the database returned no records.");
        }
    }
}
