package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.smartcampus.cms.model.Admin;

public class AdminDashboardFrame extends JFrame {

    private final Admin admin;

    public AdminDashboardFrame(Admin admin) {

        this.admin = admin;

        setTitle("Admin Dashboard");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + admin.getFullName(),
                SwingConstants.CENTER
        );

        welcomeLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        mainPanel.add(
                welcomeLabel,
                BorderLayout.NORTH
        );

        JPanel buttonPanel = new JPanel(
                new GridLayout(3, 1, 12, 12)
        );

        JButton complaintsButton =
                new JButton("View All Complaints");

        JButton assignmentButton =
                new JButton("Assign Complaints");

        JButton logoutButton =
                new JButton("Logout");

        complaintsButton.addActionListener(event -> {
            new AdminComplaintsFrame().setVisible(true);
        });

        assignmentButton.addActionListener(event -> {
            new AssignComplaintFrame().setVisible(true);
        });


        logoutButton.addActionListener(event -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        buttonPanel.add(complaintsButton);
        buttonPanel.add(assignmentButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);
    }
}