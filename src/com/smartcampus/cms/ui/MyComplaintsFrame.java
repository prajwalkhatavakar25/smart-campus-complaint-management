package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.User;

public class MyComplaintsFrame extends JFrame {

    private final User student;
    private final JTable complaintsTable;
    private final DefaultTableModel tableModel;

    public MyComplaintsFrame(User student) {

        this.student = student;

        setTitle("My Complaints");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Heading
        JLabel headingLabel = new JLabel(
                "MY COMPLAINTS",
                SwingConstants.CENTER
        );

        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel(
                new String[] {
                        "Complaint ID",
                        "Title",
                        "Category",
                        "Location",
                        "Priority",
                        "Status"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        complaintsTable = new JTable(tableModel);
        complaintsTable.setRowHeight(25);
        complaintsTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(complaintsTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load complaints from database
        loadComplaints();

        // Refresh button
        refreshButton.addActionListener(event -> loadComplaints());

        // Back button
        backButton.addActionListener(event -> {
            new StudentDashboardFrame(student).setVisible(true);
            dispose();
        });

        add(mainPanel);
    }

    private void loadComplaints() {

        tableModel.setRowCount(0);

        ComplaintDAO complaintDAO = new ComplaintDAO();

        List<Complaint> complaints =
                complaintDAO.getComplaintsByStudentId(student.getUserId());

        if (complaints.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No complaints found for your account.",
                    "My Complaints",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        for (Complaint complaint : complaints) {

            String categoryName = complaint.getCategory() != null
                    ? complaint.getCategory().getCategoryName()
                    : "N/A";

            tableModel.addRow(new Object[] {
                    complaint.getComplaintId(),
                    complaint.getTitle(),
                    categoryName,
                    complaint.getLocation(),
                    complaint.getPriority(),
                    complaint.getStatus()
            });
        }
    }
}