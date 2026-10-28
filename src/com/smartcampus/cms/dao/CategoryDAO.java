package com.smartcampus.cms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smartcampus.cms.model.Category;
import com.smartcampus.cms.util.DBConnection;

public class CategoryDAO {
	public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT category_id, category_name, description "
                   + "FROM categories ORDER BY category_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("description")
                );

                categories.add(category);
            }

        } catch (SQLException e) {
            System.out.println("Unable to fetch categories: " + e.getMessage());
        }

        return categories;
    }

}
