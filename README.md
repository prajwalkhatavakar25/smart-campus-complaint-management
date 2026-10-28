# Smart Campus Complaint Management System

A Java-based campus complaint management application developed as an Object-Oriented Programming mini-project. It enables students to submit campus complaints, administrators to assign complaints to staff, and staff to update their progress.

## Features

* **Role-based authentication:** Separate student, staff, and administrator roles.
* **Complaint submission:** Students can submit complaints about campus facilities.
* **Complaint categorization:** Organize complaints by category and priority.
* **Staff assignment:** Administrators can assign complaints to appropriate staff members.
* **Status tracking:** Track complaint progress through different workflow stages.
* **Complaint history:** Maintain a record of complaint status changes.
* **MySQL integration:** Store and retrieve application data using JDBC.
* **Report generation:** Generate complaint reports using Java threading.
* **Multiple interfaces:** Includes a console interface and a Java Swing interface.

## Tech Stack

| Technology     | Purpose                                 |
| -------------- | --------------------------------------- |
| Java 21        | Application development                 |
| MySQL          | Database management                     |
| JDBC           | Database connectivity                   |
| Java Swing     | Graphical user interface                |
| Eclipse IDE    | Development environment                 |
| Git and GitHub | Version control and source-code hosting |

## Project Structure

```text
SmartCampusComplaintManagement/
├── src/
│   └── com/smartcampus/cms/
│       ├── dao/          # Database access objects
│       ├── exception/    # Custom exceptions
│       ├── model/        # Application models
│       ├── service/      # Business logic
│       ├── ui/           # Console and Swing interfaces
│       ├── util/         # Database and utility classes
│       ├── Main.java     # Console application entry point
│       └── GuiMain.java  # Swing application entry point
├── sql/
│   └── smart_campus_cms_schema.sql
├── .gitignore
├── .classpath
├── .project
└── README.md
```

## Prerequisites

Before running the application, install:

* Java Development Kit (JDK) 21
* MySQL Server
* MySQL Connector/J
* Eclipse IDE or another compatible Java IDE

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/prajwalkhatavakar25/smart-campus-complaint-management.git
```

Import the downloaded project into Eclipse as an existing Java project.

### 2. Create the database

Open MySQL Workbench or your preferred MySQL client.

Execute the SQL script:

`sql/smart_campus_cms_schema.sql`

The script creates the `smart_campus_cms` database, the required tables, and default complaint categories.

### 3. Configure the database connection

The current application configuration uses:

* **Host:** `localhost`
* **Port:** `3307`
* **Database:** `smart_campus_cms`
* **Username:** Your local MySQL username

If your MySQL server uses another port, update the JDBC URL in `DBConnection.java`.

The database password is read from the `DB_Password` environment variable.

On Windows PowerShell, you can set this variable for the current terminal session:

```powershell
$env:DB_Password = "YOUR_LOCAL_MYSQL_PASSWORD"
```

Replace the placeholder with your own MySQL password. Never commit your actual password to GitHub.

If you launch Eclipse separately, make sure Eclipse inherits the environment variable or configure it in the application's run configuration.

### 4. Configure MySQL Connector/J

Add the MySQL Connector/J JAR to your Eclipse project's Java Build Path.

1. Right-click the project.
2. Select **Build Path → Configure Build Path**.
3. Open the **Libraries** tab.
4. Select **Add External JARs**.
5. Choose your MySQL Connector/J JAR.
6. Apply the changes.

### 5. Run the application

After configuring the database and JDBC driver, run one of these classes:

* `com.smartcampus.cms.Main` — Console application.
* `com.smartcampus.cms.GuiMain` — Java Swing interface.

Database-dependent features require a running MySQL server and correctly configured database credentials.

## Application Screenshots

Screenshots of the running application will be added here.

Suggested screenshots:

* Login screen
* 
* Student complaint submission
* Complaint list and status history
* Administrator complaint assignment
* Staff complaint status updates

Use demonstration data in screenshots. Do not publish real passwords, private complaint descriptions, or personal information.

## Security Considerations

* Keep database passwords and other credentials out of source code.
* Do not upload generated reports containing personal or confidential information.
* Use demonstration or anonymized data when showcasing the application.
* Configure database credentials locally rather than committing them to the repository.

## Future Enhancements

* Improve authentication and password security.
* Add automated tests.
* Enhance complaint filtering and reporting.
* Improve database configuration portability.
* Expand the graphical interface.

---

Developed as a Java Object-Oriented Programming mini-project.
## Application Screenshots

### Login Screen

![Login Screen](docs/screenshots/login.png)

### Student Dashboard

![Student Dashboard](docs/screenshots/student-dashboard.png)

### Complaint Submission Form

![Complaint Submission Form](docs/screenshots/complaint-form.png)
