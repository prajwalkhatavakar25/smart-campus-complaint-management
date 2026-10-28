package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.smartcampus.cms.exception.AuthenticationException;
import com.smartcampus.cms.model.User;
import com.smartcampus.cms.service.AuthenticationService;
import com.smartcampus.cms.model.Student;

public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final AuthenticationService authenticationService;

    public LoginFrame() {
        authenticationService = new AuthenticationService();

        setTitle("Smart Campus Complaint Management System");
        setSize(500, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel(
                "SMART CAMPUS COMPLAINT MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(emailLabel, constraints);

        emailField = new JTextField(22);
        constraints.gridx = 1;
        constraints.gridy = 0;
        formPanel.add(emailField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(22);
        constraints.gridx = 1;
        constraints.gridy = 1;
        formPanel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::login);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> System.exit(0));

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(loginButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        formPanel.add(exitButton, constraints);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void login(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User loggedInUser = authenticationService.login(email, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome, " + loggedInUser.getFullName()
            );

            // Dashboard windows will be added next.
            if (loggedInUser instanceof Student) {
                new StudentDashboardFrame(loggedInUser).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Student Dashboard is currently available."
                );
            }

        } catch (AuthenticationException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}