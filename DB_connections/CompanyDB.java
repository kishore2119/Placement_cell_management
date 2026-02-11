package DB_connections;

import java.sql.*;

public class CompanyDB {

    // Create
    public static boolean addCompany(String name, String location, String industry, String hrcontact) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO company(name, location, industry, hrcontact) VALUES(?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, industry);
            ps.setString(4, hrcontact);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read all
    public static ResultSet getAllCompanies() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery("SELECT * FROM company ORDER BY id");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read one
    public static ResultSet getCompanyById(int id) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM company WHERE id = ?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get id-name list (for combo boxes)
    public static ResultSet getCompanyIdNames() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery("SELECT id, name FROM company ORDER BY name");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update
    public static boolean updateCompany(int id, String name, String location, String industry, String hrcontact) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE company SET name=?, location=?, industry=?, hrcontact=? WHERE id=?");
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, industry);
            ps.setString(4, hrcontact);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public static boolean deleteCompany(int id) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM company WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
