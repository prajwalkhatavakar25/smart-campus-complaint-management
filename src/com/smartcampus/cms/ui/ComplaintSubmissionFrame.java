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

public class ComplaintSubmissionFrame extends JFrame {

    private final User student;

    private final JComboBox<String> categoryBox;
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

        categoryBox = new JComboBox<>(new String[] {
                "Electrical",
                "Plumbing",
                "WiFi and Network",
                "Classroom",
                "Hostel",
                "Sanitation",
                "Security",
                "Other"
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

        JOptionPane.showMessageDialog(
                this,
                "Form validated successfully. Database submission will be connected next."
        );
    }
}