package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import DB_connections.*;

public class AdminDashboard extends JFrame {

    private JPanel contentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu buttons at the top
        JPanel menuPanel = new JPanel(new FlowLayout());
        JButton studentsBtn = new JButton("Students");
        JButton companiesBtn = new JButton("Companies");
        JButton drivesBtn = new JButton("Drives");
        JButton appsBtn = new JButton("Applications");
        JButton logoutBtn = new JButton("Logout");
        menuPanel.add(studentsBtn);
        menuPanel.add(companiesBtn);
        menuPanel.add(drivesBtn);
        menuPanel.add(appsBtn);
        menuPanel.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout());

        studentsBtn.addActionListener(e -> showStudents());
        companiesBtn.addActionListener(e -> showCompanies());
        drivesBtn.addActionListener(e -> showDrives());
        appsBtn.addActionListener(e -> showApplications());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(menuPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        showStudents();
        setVisible(true);
    }

    // helper to swap content
    private void setContent(JScrollPane table, JPanel buttons) {
        contentPanel.removeAll();
        contentPanel.add(table, BorderLayout.CENTER);
        contentPanel.add(buttons, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---- Students Section ----
    private void showStudents() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Roll No", "Name", "Age", "Major", "GPA", "Email" }, 0);
        JTable table = new JTable(model);

        try {
            ResultSet rs = StudentDB.getAllStudents();
            while (rs != null && rs.next()) {
                model.addRow(new Object[] { rs.getString("rollnum"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("major"),
                        rs.getDouble("gpa"), rs.getString("email") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a student first.");
                return;
            }
            String roll = (String) model.getValueAt(row, 0);
            StudentDB.deleteStudent(roll);
            showStudents();
        });
        refreshBtn.addActionListener(e -> showStudents());

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(deleteBtn);
        bp.add(refreshBtn);
        setContent(new JScrollPane(table), bp);
    }

    // ---- Companies Section ----
    private void showCompanies() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Name", "Location", "Industry", "HR Contact" }, 0);
        JTable table = new JTable(model);

        try {
            ResultSet rs = CompanyDB.getAllCompanies();
            while (rs != null && rs.next()) {
                model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"),
                        rs.getString("location"), rs.getString("industry"),
                        rs.getString("hrcontact") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> {
            JTextField fN = new JTextField(), fL = new JTextField(),
                    fI = new JTextField(), fH = new JTextField();
            Object[] fields = { "Name:", fN, "Location:", fL, "Industry:", fI, "HR Contact:", fH };
            if (JOptionPane.showConfirmDialog(this, fields, "Add Company",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                CompanyDB.addCompany(fN.getText(), fL.getText(), fI.getText(), fH.getText());
                showCompanies();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a company first.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            JTextField fN = new JTextField((String) model.getValueAt(row, 1));
            JTextField fL = new JTextField((String) model.getValueAt(row, 2));
            JTextField fI = new JTextField((String) model.getValueAt(row, 3));
            JTextField fH = new JTextField((String) model.getValueAt(row, 4));
            Object[] fields = { "Name:", fN, "Location:", fL, "Industry:", fI, "HR Contact:", fH };
            if (JOptionPane.showConfirmDialog(this, fields, "Edit Company",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                CompanyDB.updateCompany(id, fN.getText(), fL.getText(), fI.getText(), fH.getText());
                showCompanies();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a company first.");
                return;
            }
            CompanyDB.deleteCompany((int) model.getValueAt(row, 0));
            showCompanies();
        });

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(addBtn);
        bp.add(editBtn);
        bp.add(deleteBtn);
        setContent(new JScrollPane(table), bp);
    }

    // ---- Drives Section ----
    private void showDrives() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Company", "Start", "End", "Seats", "LPA", "Min GPA" }, 0);
        JTable table = new JTable(model);

        try {
            ResultSet rs = DriveDB.getAllDrives();
            while (rs != null && rs.next()) {
                model.addRow(new Object[] { rs.getInt("D_id"), rs.getString("cname"),
                        rs.getString("start_date"), rs.getString("end_date"),
                        rs.getInt("availableSeats"), rs.getDouble("lpa"),
                        rs.getDouble("mingpa") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton addBtn = new JButton("Create Drive");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> {
            JComboBox<String> cb = new JComboBox<>();
            try {
                ResultSet rs = CompanyDB.getCompanyIdNames();
                while (rs != null && rs.next())
                    cb.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            JTextField fS = new JTextField("YYYY-MM-DD"), fE = new JTextField("YYYY-MM-DD"),
                    fSeats = new JTextField(), fLPA = new JTextField(), fGPA = new JTextField();
            Object[] fields = { "Company:", cb, "Start:", fS, "End:", fE,
                    "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA };
            if (JOptionPane.showConfirmDialog(this, fields, "Create Drive",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                    DriveDB.addDrive(compId, fS.getText(), fE.getText(),
                            Integer.parseInt(fSeats.getText()),
                            Double.parseDouble(fLPA.getText()),
                            Double.parseDouble(fGPA.getText()));
                    showDrives();
                } catch (Exception ex) {
                    msg("Invalid input.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a drive first.");
                return;
            }
            DriveDB.deleteDrive((int) model.getValueAt(row, 0));
            showDrives();
        });

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(addBtn);
        bp.add(deleteBtn);
        setContent(new JScrollPane(table), bp);
    }

    // ---- Applications Section ----
    private void showApplications() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "App ID", "Drive", "Company", "Student", "Name", "Date", "Status" }, 0);
        JTable table = new JTable(model);
        loadAppData(model, null);

        JComboBox<String> filterBox = new JComboBox<>(
                new String[] { "All", "Applied", "Shortlisted", "Accepted", "Rejected" });
        JButton filterBtn = new JButton("Filter");
        JButton statusBtn = new JButton("Update Status");

        filterBtn.addActionListener(e -> {
            String f = (String) filterBox.getSelectedItem();
            model.setRowCount(0);
            loadAppData(model, "All".equals(f) ? null : f);
        });

        statusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select an application first.");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            JComboBox<String> combo = new JComboBox<>(
                    new String[] { "Applied", "Shortlisted", "Accepted", "Rejected" });
            combo.setSelectedItem(model.getValueAt(row, 6));
            if (JOptionPane.showConfirmDialog(this, combo, "Update Status",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                ApplicationDB.updateStatus(appId, (String) combo.getSelectedItem());
                showApplications();
            }
        });

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(new JLabel("Filter:"));
        bp.add(filterBox);
        bp.add(filterBtn);
        bp.add(statusBtn);
        setContent(new JScrollPane(table), bp);
    }

    private void loadAppData(DefaultTableModel model, String statusFilter) {
        try {
            ResultSet rs = ApplicationDB.getAllApplications(statusFilter);
            while (rs != null && rs.next()) {
                model.addRow(new Object[] { rs.getInt("A_id"), rs.getInt("driveId"),
                        rs.getString("cname"), rs.getString("s_id"), rs.getString("sname"),
                        rs.getString("applicationDate"), rs.getString("status") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}
