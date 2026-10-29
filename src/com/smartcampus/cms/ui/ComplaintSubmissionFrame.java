package com.smartcampus.cms.ui;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.smartcampus.cms.model.Priority;
import com.smartcampus.cms.model.User;

import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.smartcampus.cms.dao.CategoryDAO;
import com.smartcampus.cms.dao.ComplaintDAO;

import com.smartcampus.cms.model.Category;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.model.Student;

public class ComplaintSubmissionFrame extends JFrame {

    private final User student;

    private final JComboBox<Category> categoryBox;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JTextField locationField;
    private final JComboBox<Priority> priorityBox;

    public ComplaintSubmissionFrame(User student) {
        this.student = student;

        setTitle("Submit a Complaint");
        setSize(600, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("SUBMIT A COMPLAINT", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Category:"), constraints);

        List<Category> categories = new CategoryDAO().getAllCategories();

        categoryBox = new JComboBox<>(
                categories.toArray(new Category[0])
        );

        categoryBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof Category) {
                    setText(((Category) value).getCategoryName());
                }

                return this;
            }
        });
        constraints.gridx = 1;
        formPanel.add(categoryBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Complaint Title:"), constraints);

        titleField = new JTextField(25);
        constraints.gridx = 1;
        formPanel.add(titleField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), constraints);

        descriptionArea = new JTextArea(6, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        constraints.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Location:"), constraints);

        locationField = new JTextField(25);
        constraints.gridx = 1;
        formPanel.add(locationField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(new JLabel("Priority:"), constraints);

        priorityBox = new JComboBox<>(Priority.values());
        constraints.gridx = 1;
        formPanel.add(priorityBox, constraints);

        JButton submitButton = new JButton("Submit Complaint");
        JButton backButton = new JButton("Back");

        submitButton.addActionListener(event -> validateForm());

        backButton.addActionListener(event -> {
            new StudentDashboardFrame(student).setVisible(true);
            dispose();
        });

        constraints.gridx = 0;
        constraints.gridy = 5;
        formPanel.add(submitButton, constraints);

        constraints.gridx = 1;
        formPanel.add(backButton, constraints);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void validateForm() {

        // Validate required fields
        if (titleField.getText().trim().isEmpty()
                || descriptionArea.getText().trim().isEmpty()
                || locationField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all complaint details.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Check category selection
        Category selectedCategory =
                (Category) categoryBox.getSelectedItem();

        if (selectedCategory == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No category available. Please check your database.",
                    "Category Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Verify the logged-in user is a Student
        if (!(student instanceof Student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid student account.",
                    "Authentication Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Create the complaint object
        Complaint complaint = new Complaint();

        complaint.setStudent((Student) student);
        complaint.setCategory(selectedCategory);
        complaint.setTitle(titleField.getText().trim());
        complaint.setDescription(descriptionArea.getText().trim());
        complaint.setLocation(locationField.getText().trim());
        complaint.setPriority((Priority) priorityBox.getSelectedItem());
        complaint.setStatus(ComplaintStatus.SUBMITTED);

        // Save the complaint to MySQL
        ComplaintDAO complaintDAO = new ComplaintDAO();

        boolean success = complaintDAO.createComplaint(complaint);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complaint submitted successfully!\n"
                            + "Complaint ID: " + complaint.getComplaintId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Return to Student Dashboard
            new StudentDashboardFrame(student).setVisible(true);
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to submit complaint.\n"
                            + "Please check your database connection and try again.",
                    "Submission Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}