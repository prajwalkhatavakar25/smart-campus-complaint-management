package com.smartcampus.cms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smartcampus.cms.util.DBConnection;

public class ComplaintHistoryDAO {

    public List<Object[]> getHistoryByStudentId(int studentId) {

        List<Object[]> history = new ArrayList<>();

        String sql =
                "SELECT c.complaint_id, c.title, " +
                "cu.old_status, cu.new_status, " +
                "cu.update_message, u.full_name AS updated_by_name, " +
                "cu.updated_at " +
                "FROM complaint_updates cu " +
                "JOIN complaints c ON cu.complaint_id = c.complaint_id " +
                "LEFT JOIN users u ON cu.updated_by = u.user_id " +
                "WHERE c.student_id = ? " +
                "ORDER BY cu.updated_at DESC, cu.update_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Object[] row = {
                            rs.getInt("complaint_id"),
                            rs.getString("title"),
                            rs.getString("old_status"),
                            rs.getString("new_status"),
                            rs.getString("update_message"),
                            rs.getString("updated_by_name"),
                            rs.getTimestamp("updated_at")
                    };

                    history.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Unable to fetch complaint history: "
                            + e.getMessage()
            );
        }

        return history;
    }
}

