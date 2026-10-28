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
}