package DB_connections;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    
    public static void setupDatabase() {
        try (Connection c = connecton.getConnection()) {
            if (c == null) {
                System.out.println("Failed to connect to database");
                return;
            }
            
            Statement stmt = c.createStatement();
            
            // Drop existing tables
            System.out.println("Dropping existing tables...");
            try {
                stmt.execute("DROP TABLE IF EXISTS applications");
                stmt.execute("DROP TABLE IF EXISTS drive");
                stmt.execute("DROP TABLE IF EXISTS student");
                stmt.execute("DROP TABLE IF EXISTS company");
                System.out.println("Old tables dropped successfully");
            } catch (Exception e) {
                System.out.println("Some tables did not exist (this is fine)");
            }
            
            // Create student table with password column
            System.out.println("Creating student table...");
            stmt.execute("CREATE TABLE student (" +
                    "rollnum VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "age INTEGER," +
                    "major VARCHAR(100) NOT NULL," +
                    "gpa DECIMAL(4, 2) NOT NULL," +
                    "password VARCHAR(150) NOT NULL)");
            
            // Create company table
            System.out.println("Creating company table...");
            stmt.execute("CREATE TABLE company (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(150) NOT NULL UNIQUE," +
                    "location VARCHAR(200)," +
                    "industry VARCHAR(100)," +
                    "hrcontact VARCHAR(150) NOT NULL UNIQUE)");
            
            // Create drive table
            System.out.println("Creating drive table...");
            stmt.execute("CREATE TABLE drive (" +
                    "D_id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                    "companyId INTEGER NOT NULL," +
                    "start_date DATE NOT NULL," +
                    "end_date DATE NOT NULL," +
                    "availableSeats INT NOT NULL," +
                    "lpa DECIMAL(10, 2) NOT NULL," +
                    "mingpa DECIMAL(4, 2) NOT NULL," +
                    "FOREIGN KEY (companyId) REFERENCES company (id))");
            
            // Create applications table
            System.out.println("Creating applications table...");
            stmt.execute("CREATE TABLE applications (" +
                    "A_id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                    "driveId INTEGER NOT NULL," +
                    "s_id VARCHAR(50) NOT NULL," +
                    "applicationDate DATE NOT NULL," +
                    "status VARCHAR(20) NOT NULL," +
                    "FOREIGN KEY (driveId) REFERENCES drive (D_id)," +
                    "FOREIGN KEY (s_id) REFERENCES student (rollnum)," +
                    "CHECK (status IN ('Applied', 'Shortlisted', 'Rejected', 'Accepted')))");
            
            System.out.println("\n✓ Database setup completed successfully!");
            System.out.println("All tables created with password column instead of email");
            
        } catch (Exception e) {
            System.out.println("Error during database setup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Starting database setup...");
        setupDatabase();
    }
}
