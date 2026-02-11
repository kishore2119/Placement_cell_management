# Placement Cell Management System

A Java Swing desktop application for managing campus placement drives. Provides separate dashboards for **Admin** and **Student** users, backed by a MySQL database.

---

## Features

### Admin Dashboard
- **Student Management** — View and delete registered students
- **Company Management** — Add, edit, and delete companies
- **Drive Management** — Create and delete placement drives (linked to companies)
- **Application Management** — View all applications, filter by status, update status (Applied → Shortlisted → Accepted / Rejected)

### Student Dashboard
- **Self-Registration** — Students register from the login screen
- **Profile Management** — View and update personal information
- **Browse Drives** — View available placement drives
- **Apply to Drives** — Apply with automatic GPA eligibility check and duplicate detection
- **Track Applications** — View application status and withdraw pending applications

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 8+ |
| GUI | Java Swing |
| Database | MySQL |
| JDBC Driver | MySQL Connector/J |

---

## Database Setup

1. Create a MySQL database named `placement_db`:
   ```sql
   CREATE DATABASE placement_db;
   USE placement_db;
   ```
2. Run the schema file to create tables:
   ```sql
   source schema.sql;
   ```
3. Update DB credentials in `DB_connections/connecton.java` if needed (default: `root` with no password on `localhost:3306`).

---

## Project Structure

```
├── Main.java                  # Application entry point
├── Student.java               # Student model
├── Company.java               # Company model
├── Drive.java                 # Placement drive model
├── Application.java           # Application model
├── Admin.java                 # Admin business logic
├── schema.sql                 # Database schema (DDL)
├── DB_connections/
│   ├── connecton.java         # JDBC connection helper
│   ├── StudentDB.java         # Student CRUD operations
│   ├── CompanyDB.java         # Company CRUD operations
│   ├── DriveDB.java           # Drive CRUD operations
│   └── ApplicationDB.java     # Application CRUD operations
└── gui/
    ├── LoginFrame.java        # Login & registration screen
    ├── AdminDashboard.java    # Admin panel (tabbed)
    └── StudentDashboard.java  # Student panel (tabbed)
```

---

## How to Compile & Run

### Prerequisites
- JDK 8 or later
- MySQL Server running with `placement_db` created
- MySQL Connector/J JAR (e.g., `mysql-connector-j-8.x.x.jar`)

### Compile
```bash
javac -cp .:mysql-connector-j-8.x.x.jar DB_connections/*.java gui/*.java *.java
```

### Run
```bash
java -cp .:mysql-connector-j-8.x.x.jar Main
```

> On Windows, replace `:` with `;` in the classpath.

---

## Default Login

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin` |
| Student | Roll Number | Email |

---

## License

This project is for educational purposes.
