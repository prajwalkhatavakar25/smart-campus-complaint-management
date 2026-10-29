package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.dao.ComplaintUpdateDAO;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.model.Staff;

public class StaffComplaintsFrame extends JFrame {

    private final Staff staff;
    private final DefaultTableModel tableModel;
    private final JTable complaintsTable;

    public StaffComplaintsFrame(Staff staff) {

        this.staff = staff;

        setTitle("Assigned Complaints");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel(
                "ASSIGNED COMPLAINTS",
                SwingConstants.CENTER
        );

        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // Complaints table
        tableModel = new DefaultTableModel(
                new String[]{
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

        mainPanel.add(
                new JScrollPane(complaintsTable),
                BorderLayout.CENTER
        );

        // Buttons
        JButton updateStatusButton =
                new JButton("Update Status");

        JButton refreshButton =
                new JButton("Refresh");

        JButton backButton =
                new JButton("Back");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        buttonPanel.add(updateStatusButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Refresh complaints
        refreshButton.addActionListener(
                event -> loadComplaints()
        );

        // Update complaint status
        updateStatusButton.addActionListener(
                event -> updateComplaintStatus()
        );

        // Return to dashboard
        backButton.addActionListener(event -> {
            new StaffDashboardFrame(staff).setVisible(true);
            dispose();
        });

        add(mainPanel);

        loadComplaints();
    }

    private void loadComplaints() {

        tableModel.setRowCount(0);

        ComplaintDAO dao = new ComplaintDAO();

        List<Complaint> complaints =
                dao.getComplaintsByStaffId(staff.getUserId());

        for (Complaint complaint : complaints) {

            String categoryName =
                    complaint.getCategory() != null
                            ? complaint.getCategory().getCategoryName()
                            : "N/A";

            tableModel.addRow(new Object[]{
                    complaint.getComplaintId(),
                    complaint.getTitle(),
                    categoryName,
                    complaint.getLocation(),
                    complaint.getPriority(),
                    complaint.getStatus()
            });
        }
    }

    private void updateComplaintStatus() {

        // Check whether a complaint is selected
        int selectedRow = complaintsTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a complaint first.",
                    "No Complaint Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convert the displayed row to the model row
        int modelRow =
                complaintsTable.convertRowIndexToModel(selectedRow);

        int complaintId = (int) tableModel.getValueAt(
                modelRow, 0
        );

        String currentStatus = tableModel.getValueAt(
                modelRow, 5
        ).toString();

        // Available statuses
        JComboBox<ComplaintStatus> statusBox =
                new JComboBox<>(
                        new ComplaintStatus[]{
                                ComplaintStatus.IN_PROGRESS,
                                ComplaintStatus.RESOLVED
                        }
                );

        // Set a default selection
        if (currentStatus.equals("IN_PROGRESS")) {
            statusBox.setSelectedItem(
                    ComplaintStatus.RESOLVED
            );
        } else {
            statusBox.setSelectedItem(
                    ComplaintStatus.IN_PROGRESS
            );
        }

        JTextArea messageArea = new JTextArea(4, 25);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JPanel updatePanel = new JPanel(
                new GridLayout(0, 1, 5, 5)
        );

        updatePanel.add(new JLabel("Complaint ID: " + complaintId));
        updatePanel.add(new JLabel("Current Status: " + currentStatus));
        updatePanel.add(new JLabel("Select New Status:"));
        updatePanel.add(statusBox);
        updatePanel.add(new JLabel("Update Message:"));
        updatePanel.add(new JScrollPane(messageArea));

        int result = JOptionPane.showConfirmDialog(
                this,
                updatePanel,
                "Update Complaint Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        ComplaintStatus newStatus =
                (ComplaintStatus) statusBox.getSelectedItem();

        String message = messageArea.getText().trim();

        if (newStatus == null) {
            return;
        }

        if (newStatus.name().equals(currentStatus)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a different status.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (message.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an update message.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Save status and history together
        ComplaintUpdateDAO updateDAO =
                new ComplaintUpdateDAO();

        boolean success = updateDAO.updateStatusWithHistory(
                complaintId,
                staff.getUserId(),
                newStatus,
                message
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complaint status updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadComplaints();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update the complaint.\n"
                            + "Check the database connection, "
                            + "staff assignment, and console errors.",
                    "Update Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}