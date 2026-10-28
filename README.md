# Smart Campus Complaint Management System

A Java OOPJ mini project that helps students submit campus complaints, administrators assign them to staff, and staff update their progress.

## Features

- Role-based authentication: Student, Admin, and Staff
- Complaint submission, assignment, status updates, and history
- JDBC-based MySQL persistence
- Validation of complaint-status transitions
- Background complaint report generation using `Runnable`
- Console interface and an in-progress Java Swing interface

## Requirements

- Java 21
- MySQL Server
- MySQL Connector/J
- Eclipse IDE (recommended)

## Database configuration

For security, local database credentials are not included in this repository.

1. Copy `src/com/smartcampus/cms/util/DBConnection.java.example` to `DBConnection.java` in the same folder.
2. Replace `YOUR_MYSQL_USERNAME` and `YOUR_MYSQL_PASSWORD` with your local MySQL credentials.
3. Run `sql/smart_campus_cms_schema.sql` in MySQL.
4. Add MySQL Connector/J to the Java build path.
5. Run `com.smartcampus.cms.Main` for the console version or `com.smartcampus.cms.GuiMain` for the Swing login screen.

## Team

- Prajwal Khatavakar — 1RUA25SCS0071
- Sanjay M — 1RUA25SCS0089
- Prajwal R — 1RUA25SCS0072
- Prajwal J — 1RUA25SCS0070
- Nithin L — 1RUA25SCS0065