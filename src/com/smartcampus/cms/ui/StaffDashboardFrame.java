package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.smartcampus.cms.model.Staff;

public class StaffDashboardFrame extends JFrame {

    private final Staff staff;

    public StaffDashboardFrame(Staff staff) {

        this.staff = staff;

        setTitle("Staff Dashboard");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + staff.getFullName(),
                SwingConstants.CENTER
        );

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(
                new GridLayout(3, 1, 12, 12)
        );

        JButton assignedButton =
                new JButton("View Assigned Complaints");

        JButton refreshButton =
                new JButton("Refresh Dashboard");

        JButton logoutButton =
                new JButton("Logout");

        assignedButton.addActionListener(event -> {
            new StaffComplaintsFrame(staff).setVisible(true);
            dispose();
        });

        refreshButton.addActionListener(event -> {
            getContentPane().revalidate();
            getContentPane().repaint();
        });

        logoutButton.addActionListener(event -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        buttonPanel.add(assignedButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
}
