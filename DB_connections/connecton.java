package DB_connections;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class connecton {
    private static final String URL = "jdbc:mysql://localhost:3306/placement_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "MySQL JDBC Driver not found.\nMake sure mysql-connector-j JAR is in the lib/ folder.",
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Database connection failed:\n" + e.getMessage()
                            + "\n\nMake sure MySQL is running and 'placement_db' exists.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
}
