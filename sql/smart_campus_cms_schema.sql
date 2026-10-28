CREATE DATABASE IF NOT EXISTS smart_campus_cms;
USE smart_campus_cms;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'STAFF', 'ADMIN') NOT NULL,
    department VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    category_id INT NOT NULL,
    assigned_staff_id INT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(150) NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH')
        NOT NULL DEFAULT 'MEDIUM',
    status ENUM(
        'SUBMITTED',
        'ASSIGNED',
        'IN_PROGRESS',
        'RESOLVED',
        'CLOSED'
    ) NOT NULL DEFAULT 'SUBMITTED',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (assigned_staff_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS complaint_updates (
    update_id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    updated_by INT NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30),
    update_message TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id)
        ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(user_id)
);

INSERT IGNORE INTO categories
    (category_name, description)
VALUES
    ('Electrical', 'Lights, fans, sockets, or power problems'),
    ('Plumbing', 'Water, taps, washrooms, or drainage problems'),
    ('WiFi and Network', 'Internet or network connectivity problems'),
    ('Classroom', 'Furniture, projector, board, or classroom facilities'),
    ('Hostel', 'Hostel room or hostel facility complaints'),
    ('Sanitation', 'Cleaning, waste disposal, or hygiene issues'),
    ('Security', 'Campus safety or security issues'),
    ('Other', 'Any complaint not covered by another category');