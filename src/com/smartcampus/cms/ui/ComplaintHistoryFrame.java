package com.smartcampus.cms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.smartcampus.cms.dao.ComplaintHistoryDAO;
import com.smartcampus.cms.model.User;

public class ComplaintHistoryFrame extends JFrame {

    private final User student;
    private final DefaultTableModel tableModel;

    public ComplaintHistoryFrame(User student) {

        this.student = student;

        setTitle("Complaint History");
        setSize(1100, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel(
                "COMPLAINT HISTORY",
                SwingConstants.CENTER
        );

        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        mainPanel.add(headingLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] {
                        "Complaint ID",
                        "Title",
                        "Previous Status",
                        "New Status",
                        "Update Message",
                        "Updated By",
                        "Updated At"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historyTable = new JTable(tableModel);
        historyTable.setRowHeight(28);
        historyTable.setAutoCreateRowSorter(true);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        historyTable.getColumnModel()
                .getColumn(0).setPreferredWidth(90);
        historyTable.getColumnModel()
                .getColumn(1).setPreferredWidth(180);
        historyTable.getColumnModel()
                .getColumn(2).setPreferredWidth(130);
        historyTable.getColumnModel()
                .getColumn(3).setPreferredWidth(130);
        historyTable.getColumnModel()
                .getColumn(4).setPreferredWidth(300);
        historyTable.getColumnModel()
                .getColumn(5).setPreferredWidth(150);
        historyTable.getColumnModel()
                .getColumn(6).setPreferredWidth(160);

        mainPanel.add(
                new JScrollPane(historyTable),
                BorderLayout.CENTER
        );

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                event -> loadHistory()
        );

        backButton.addActionListener(event -> {
            new StudentDashboardFrame(student).setVisible(true);
            dispose();
        });

        add(mainPanel);

        loadHistory();
    }

    private void loadHistory() {

        tableModel.setRowCount(0);

        ComplaintHistoryDAO dao = new ComplaintHistoryDAO();

        List<Object[]> history =
                dao.getHistoryByStudentId(student.getUserId());

        for (Object[] row : history) {
            tableModel.addRow(row);
        }

        if (history.isEmpty()) {
            setTitle("Complaint History - No History Records");
        } else {
            setTitle("Complaint History");
        }
    }
}
