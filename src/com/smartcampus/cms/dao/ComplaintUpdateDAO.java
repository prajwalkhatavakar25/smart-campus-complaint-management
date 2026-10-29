package com.smartcampus.cms.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ComplaintUpdateDAO {

    public boolean createUpdate(int complaintId, int updatedByUserId,
                                ComplaintStatus oldStatus,
                                ComplaintStatus newStatus,
                                String updateMessage) {

        String sql = "INSERT INTO complaint_updates "
                + "(complaint_id, updated_by, old_status, new_status, "
                + "update_message) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);
            statement.setInt(2, updatedByUserId);
            statement.setString(3, oldStatus.name());
            statement.setString(4, newStatus.name());
            statement.setString(5, updateMessage);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                    "Unable to save complaint update: "
                            + e.getMessage()
            );
        }

        return false;
    }
    
    public List<String> getUpdateHistoryByComplaintId(
            int complaintId) {

        List<String> history = new ArrayList<>();

        String sql = "SELECT cu.old_status, cu.new_status, "
                + "cu.update_message, cu.updated_at, "
                + "u.full_name "
                + "FROM complaint_updates cu "
                + "JOIN users u ON cu.updated_by = u.user_id "
                + "WHERE cu.complaint_id = ? "
                + "ORDER BY cu.updated_at ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                while (resultSet.next()) {
                    Timestamp timestamp =
                            resultSet.getTimestamp("updated_at");

                    String record =
                            timestamp.toLocalDateTime()
                            + " | " + resultSet.getString("full_name")
                            + " | " + resultSet.getString("old_status")
                            + " → " + resultSet.getString("new_status")
                            + " | "
                            + resultSet.getString("update_message");

                    history.add(record);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to fetch complaint history: "
                            + e.getMessage()
            );
        }

        return history;
    }
    public boolean updateStatusWithHistory(
            int complaintId,
            int staffId,
            ComplaintStatus newStatus,
            String updateMessage) {

        String selectSql =
                "SELECT status FROM complaints "
                + "WHERE complaint_id = ? AND assigned_staff_id = ?";

        String updateSql =
                "UPDATE complaints SET status = ? "
                + "WHERE complaint_id = ? AND assigned_staff_id = ?";

        String historySql =
                "INSERT INTO complaint_updates "
                + "(complaint_id, updated_by, old_status, "
                + "new_status, update_message) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                ComplaintStatus oldStatus;

                // Verify assignment and retrieve the current status.
                try (PreparedStatement statement =
                             connection.prepareStatement(selectSql)) {

                    statement.setInt(1, complaintId);
                    statement.setInt(2, staffId);

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }

                        oldStatus = ComplaintStatus.valueOf(
                                rs.getString("status")
                        );
                    }
                }

                // Avoid recording an unnecessary status change.
                if (oldStatus == newStatus) {
                    connection.rollback();
                    return false;
                }

                // Update the complaint.
                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {

                    statement.setString(1, newStatus.name());
                    statement.setInt(2, complaintId);
                    statement.setInt(3, staffId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                // Record the status transition in the history table.
                try (PreparedStatement statement =
                             connection.prepareStatement(historySql)) {

                    statement.setInt(1, complaintId);
                    statement.setInt(2, staffId);
                    statement.setString(3, oldStatus.name());
                    statement.setString(4, newStatus.name());
                    statement.setString(5, updateMessage);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;

            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println(
                            "Unable to restore auto-commit: "
                                    + e.getMessage()
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Unable to update complaint status: "
                            + e.getMessage()
            );

            return false;
        }
    }
}