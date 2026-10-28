package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.smartcampus.cms.model.User;

public class StudentDashboardFrame extends JFrame {

    private final User student;

    public StudentDashboardFrame(User student) {
        this.student = student;

        setTitle("Student Dashboard");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + student.getFullName(),
                SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 12, 12));

        JButton submitButton = new JButton("Submit a Complaint");
        JButton viewButton = new JButton("View My Complaints");
        JButton historyButton = new JButton("View Complaint History");
        JButton logoutButton = new JButton("Logout");

        submitButton.addActionListener(event -> {
            new ComplaintSubmissionFrame(student).setVisible(true);
            dispose();
        });

        viewButton.addActionListener(event ->
                JOptionPane.showMessageDialog(this,
                        "Complaint list will be added next.")
        );

        historyButton.addActionListener(event ->
                JOptionPane.showMessageDialog(this,
                        "Complaint history screen will be added later.")
        );

        logoutButton.addActionListener(event -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}