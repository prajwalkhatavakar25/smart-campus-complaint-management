package com.smartcampus.cms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.smartcampus.cms.model.Category;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;
import com.smartcampus.cms.model.Priority;
import com.smartcampus.cms.model.Staff;
import com.smartcampus.cms.model.Student;
import com.smartcampus.cms.util.DBConnection;

public class ComplaintDAO {

    public boolean createComplaint(Complaint complaint) {

        String sql = "INSERT INTO complaints "
                + "(student_id, category_id, assigned_staff_id, title, "
                + "description, location, priority, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, complaint.getStudent().getUserId());
            statement.setInt(2, complaint.getCategory().getCategoryId());

            if (complaint.getAssignedStaff() == null) {
                statement.setNull(3, Types.INTEGER);
            } else {
                statement.setInt(3,
                        complaint.getAssignedStaff().getUserId());
            }

            statement.setString(4, complaint.getTitle());
            statement.setString(5, complaint.getDescription());
            statement.setString(6, complaint.getLocation());
            statement.setString(7, complaint.getPriority().name());
            statement.setString(8, complaint.getStatus().name());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        complaint.setComplaintId(
                                generatedKeys.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to create complaint: " + e.getMessage()
            );
        }

        return false;
    }

    public List<Complaint> getComplaintsByStudentId(int studentId) {

        List<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT c.*, "
                + "ca.category_name, ca.description AS category_description, "
                + "s.full_name AS staff_name, "
                + "s.email AS staff_email, "
                + "s.department AS staff_department "
                + "FROM complaints c "
                + "JOIN categories ca ON c.category_id = ca.category_id "
                + "LEFT JOIN users s ON c.assigned_staff_id = s.user_id "
                + "WHERE c.student_id = ? "
                + "ORDER BY c.submitted_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    complaints.add(mapComplaint(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to fetch complaints: " + e.getMessage()
            );
        }

        return complaints;
    }

    private Complaint mapComplaint(ResultSet resultSet)
            throws SQLException {

        Complaint complaint = new Complaint();

        Student student = new Student();
        student.setUserId(resultSet.getInt("student_id"));

        Category category = new Category(
                resultSet.getInt("category_id"),
                resultSet.getString("category_name"),
                resultSet.getString("category_description")
        );

        complaint.setComplaintId(
                resultSet.getInt("complaint_id")
        );
        complaint.setStudent(student);
        complaint.setCategory(category);
        complaint.setTitle(resultSet.getString("title"));
        complaint.setDescription(resultSet.getString("description"));
        complaint.setLocation(resultSet.getString("location"));

        complaint.setPriority(Priority.valueOf(
                resultSet.getString("priority")
        ));

        complaint.setStatus(ComplaintStatus.valueOf(
                resultSet.getString("status")
        ));

        Timestamp submittedTimestamp =
                resultSet.getTimestamp("submitted_at");

        Timestamp updatedTimestamp =
                resultSet.getTimestamp("updated_at");

        if (submittedTimestamp != null) {
            complaint.setSubmittedAt(
                    submittedTimestamp.toLocalDateTime()
            );
        }

        if (updatedTimestamp != null) {
            complaint.setUpdatedAt(
                    updatedTimestamp.toLocalDateTime()
            );
        }

        Integer staffId = (Integer) resultSet.getObject(
                "assigned_staff_id"
        );

        if (staffId != null) {
            Staff staff = new Staff(
                    staffId,
                    resultSet.getString("staff_name"),
                    resultSet.getString("staff_email"),
                    "",
                    resultSet.getString("staff_department")
            );

            complaint.setAssignedStaff(staff);
        }

        return complaint;
    }
    
    public Complaint getComplaintById(int complaintId) {

        String sql = "SELECT c.*, "
                + "ca.category_name, ca.description AS category_description, "
                + "s.full_name AS staff_name, "
                + "s.email AS staff_email, "
                + "s.department AS staff_department "
                + "FROM complaints c "
                + "JOIN categories ca ON c.category_id = ca.category_id "
                + "LEFT JOIN users s ON c.assigned_staff_id = s.user_id "
                + "WHERE c.complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapComplaint(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to fetch complaint: " + e.getMessage()
            );
        }

        return null;
    }

    public boolean assignComplaint(int complaintId, int staffId) {

        String sql = "UPDATE complaints "
                + "SET assigned_staff_id = ?, status = 'ASSIGNED' "
                + "WHERE complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, staffId);
            statement.setInt(2, complaintId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                    "Unable to assign complaint: " + e.getMessage()
            );
        }

        return false;
    }

    public boolean updateComplaintStatus(int complaintId,
                                         ComplaintStatus newStatus) {

        String sql = "UPDATE complaints SET status = ? "
                + "WHERE complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus.name());
            statement.setInt(2, complaintId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                    "Unable to update complaint status: "
                            + e.getMessage()
            );
        }

        return false;
    }
    
    public List<Complaint> getAllComplaints() {

        List<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT c.*, "
                + "ca.category_name, ca.description AS category_description, "
                + "s.full_name AS staff_name, "
                + "s.email AS staff_email, "
                + "s.department AS staff_department "
                + "FROM complaints c "
                + "JOIN categories ca ON c.category_id = ca.category_id "
                + "LEFT JOIN users s ON c.assigned_staff_id = s.user_id "
                + "ORDER BY c.submitted_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                complaints.add(mapComplaint(resultSet));
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to fetch all complaints: " + e.getMessage()
            );
        }

        return complaints;
    }

    public List<Complaint> getComplaintsByStaffId(int staffId) {

        List<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT c.*, "
                + "ca.category_name, ca.description AS category_description, "
                + "s.full_name AS staff_name, "
                + "s.email AS staff_email, "
                + "s.department AS staff_department "
                + "FROM complaints c "
                + "JOIN categories ca ON c.category_id = ca.category_id "
                + "LEFT JOIN users s ON c.assigned_staff_id = s.user_id "
                + "WHERE c.assigned_staff_id = ? "
                + "ORDER BY c.submitted_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, staffId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    complaints.add(mapComplaint(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to fetch staff complaints: " + e.getMessage()
            );
        }

        return complaints;
    }

    public boolean deleteComplaint(int complaintId) {

        String sql = "DELETE FROM complaints WHERE complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                    "Unable to delete complaint: " + e.getMessage()
            );
        }

        return false;
    }
}