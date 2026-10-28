package com.smartcampus.cms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL =
            "jdbc:mysql://localhost:3307/smart_campus_cms";

    private static final String USERNAME = "root";

    private static final String PASSWORD = System.getenv("DB_Password");

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "DB_PASSWORD environment variable is not configured."
            );
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }}