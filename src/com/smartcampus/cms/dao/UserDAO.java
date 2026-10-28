package com.smartcampus.cms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.smartcampus.cms.model.Admin;
import com.smartcampus.cms.model.Staff;
import com.smartcampus.cms.model.Student;
import com.smartcampus.cms.model.User;
import com.smartcampus.cms.model.UserRole;
import com.smartcampus.cms.util.DBConnection;

public class UserDAO {

    public boolean createUser(User user) {

        String sql = "INSERT INTO users "
                + "(full_name, email, password_hash, role, department) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole().name());
            statement.setString(5, user.getDepartment());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Unable to create user: " + e.getMessage());
        }

        return false;
    }

    public User getUserByEmail(String email) {

        String sql = "SELECT user_id, full_name, email, password_hash, "
                + "role, department FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to find user: " + e.getMessage());
        }

        return null;
    }

    private User mapUser(ResultSet resultSet) throws SQLException {

        int userId = resultSet.getInt("user_id");
        String fullName = resultSet.getString("full_name");
        String email = resultSet.getString("email");
        String passwordHash = resultSet.getString("password_hash");
        String department = resultSet.getString("department");

        UserRole role = UserRole.valueOf(
                resultSet.getString("role")
        );

        return switch (role) {
            case STUDENT -> new Student(
                    userId, fullName, email, passwordHash, department
            );

            case STAFF -> new Staff(
                    userId, fullName, email, passwordHash, department
            );

            case ADMIN -> new Admin(
                    userId, fullName, email, passwordHash, department
            );
        };
    }
}