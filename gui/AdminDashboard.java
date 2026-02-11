package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import DB_connections.*;

public class AdminDashboard extends JFrame {

    private DefaultTableModel studentModel, companyModel, driveModel, appModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Placement Cell");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", buildStudentsTab());
        tabs.addTab("Companies", buildCompaniesTab());
        tabs.addTab("Drives", buildDrivesTab());
        tabs.addTab("Applications", buildApplicationsTab());

        // top bar with logout
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel titleLbl = new JLabel("  Admin Panel");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        loadAllData();
        setVisible(true);
    }

    // ==================== Students Tab (View & Delete only — students register
    // themselves) ====================
    private JPanel buildStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentModel = new DefaultTableModel(
                new String[] { "Roll No", "Name", "Age", "Major", "GPA", "Email" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(studentModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a student first.");
                return;
            }
            String roll = (String) studentModel.getValueAt(row, 0);
            if (confirm("Delete student " + roll + "?")) {
                if (StudentDB.deleteStudent(roll)) {
                    loadStudents();
                } else {
                    msg("Delete failed. Student may have applications.");
                }
            }
        });

        refreshBtn.addActionListener(e -> loadStudents());

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Companies Tab ====================
    private JPanel buildCompaniesTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        companyModel = new DefaultTableModel(
                new String[] { "ID", "Name", "Location", "Industry", "HR Contact" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(companyModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        addBtn.addActionListener(e -> {
            JTextField fName = new JTextField(), fLoc = new JTextField(),
                    fInd = new JTextField(), fHR = new JTextField();
            Object[] fields = { "Name:", fName, "Location:", fLoc,
                    "Industry:", fInd, "HR Contact:", fHR };
            if (JOptionPane.showConfirmDialog(this, fields, "Add Company",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (CompanyDB.addCompany(fName.getText().trim(), fLoc.getText().trim(),
                        fInd.getText().trim(), fHR.getText().trim())) {
                    loadCompanies();
                } else {
                    msg("Failed to add company.");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a company first.");
                return;
            }
            int id = (int) companyModel.getValueAt(row, 0);
            JTextField fName = new JTextField((String) companyModel.getValueAt(row, 1));
            JTextField fLoc = new JTextField((String) companyModel.getValueAt(row, 2));
            JTextField fInd = new JTextField((String) companyModel.getValueAt(row, 3));
            JTextField fHR = new JTextField((String) companyModel.getValueAt(row, 4));
            Object[] fields = { "Name:", fName, "Location:", fLoc,
                    "Industry:", fInd, "HR Contact:", fHR };
            if (JOptionPane.showConfirmDialog(this, fields, "Edit Company",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (CompanyDB.updateCompany(id, fName.getText().trim(), fLoc.getText().trim(),
                        fInd.getText().trim(), fHR.getText().trim())) {
                    loadCompanies();
                } else {
                    msg("Failed to update company.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a company first.");
                return;
            }
            int id = (int) companyModel.getValueAt(row, 0);
            if (confirm("Delete company ID " + id + "?")) {
                if (CompanyDB.deleteCompany(id)) {
                    loadCompanies();
                } else {
                    msg("Delete failed. Company may have drives.");
                }
            }
        });

        refreshBtn.addActionListener(e -> loadCompanies());

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Drives Tab ====================
    private JPanel buildDrivesTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        driveModel = new DefaultTableModel(
                new String[] { "ID", "Company", "Start Date", "End Date", "Seats", "LPA", "Min GPA" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(driveModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Create Drive");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        addBtn.addActionListener(e -> {
            JComboBox<String> fCompany = new JComboBox<>();
            loadCompanyCombo(fCompany);
            JTextField fStart = new JTextField("YYYY-MM-DD");
            JTextField fEnd = new JTextField("YYYY-MM-DD");
            JTextField fSeats = new JTextField();
            JTextField fLPA = new JTextField();
            JTextField fGPA = new JTextField();
            Object[] fields = { "Company:", fCompany, "Start Date:", fStart,
                    "End Date:", fEnd, "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA };
            if (JOptionPane.showConfirmDialog(this, fields, "Create Drive",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String sel = (String) fCompany.getSelectedItem();
                    int compId = Integer.parseInt(sel.split(" - ")[0].trim());
                    if (DriveDB.addDrive(compId, fStart.getText().trim(), fEnd.getText().trim(),
                            Integer.parseInt(fSeats.getText().trim()),
                            Double.parseDouble(fLPA.getText().trim()),
                            Double.parseDouble(fGPA.getText().trim()))) {
                        loadDrives();
                    } else {
                        msg("Failed to create drive.");
                    }
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a drive first.");
                return;
            }
            int id = (int) driveModel.getValueAt(row, 0);
            if (confirm("Delete drive ID " + id + "?")) {
                if (DriveDB.deleteDrive(id)) {
                    loadDrives();
                } else {
                    msg("Delete failed. Drive may have applications.");
                }
            }
        });

        refreshBtn.addActionListener(e -> loadDrives());

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Applications Tab ====================
    private JPanel buildApplicationsTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appModel = new DefaultTableModel(
                new String[] { "App ID", "Drive ID", "Company", "Student Roll",
                        "Student Name", "Date", "Status" },
                0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(appModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> filterCombo = new JComboBox<>(
                new String[] { "All", "Applied", "Shortlisted", "Accepted", "Rejected" });
        JButton filterBtn = new JButton("Filter");
        JButton statusBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(new JLabel("Status:"));
        btnPanel.add(filterCombo);
        btnPanel.add(filterBtn);
        btnPanel.add(statusBtn);
        btnPanel.add(refreshBtn);

        filterBtn.addActionListener(e -> {
            String f = (String) filterCombo.getSelectedItem();
            loadApplications("All".equals(f) ? null : f);
        });

        statusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select an application first.");
                return;
            }
            int appId = (int) appModel.getValueAt(row, 0);
            String current = (String) appModel.getValueAt(row, 6);
            JComboBox<String> combo = new JComboBox<>(
                    new String[] { "Applied", "Shortlisted", "Accepted", "Rejected" });
            combo.setSelectedItem(current);
            if (JOptionPane.showConfirmDialog(this, combo, "Update Status",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (ApplicationDB.updateStatus(appId, (String) combo.getSelectedItem())) {
                    loadApplications(null);
                } else {
                    msg("Failed to update status.");
                }
            }
        });

        refreshBtn.addActionListener(e -> loadApplications(null));

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Data Loading (using DAOs) ====================
    private void loadAllData() {
        loadStudents();
        loadCompanies();
        loadDrives();
        loadApplications(null);
    }

    private void loadStudents() {
        studentModel.setRowCount(0);
        try {
            ResultSet rs = StudentDB.getAllStudents();
            if (rs == null)
                return;
            while (rs.next()) {
                studentModel.addRow(new Object[] {
                        rs.getString("rollnum"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("major"), rs.getDouble("gpa"), rs.getString("email")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadCompanies() {
        companyModel.setRowCount(0);
        try {
            ResultSet rs = CompanyDB.getAllCompanies();
            if (rs == null)
                return;
            while (rs.next()) {
                companyModel.addRow(new Object[] {
                        rs.getInt("id"), rs.getString("name"), rs.getString("location"),
                        rs.getString("industry"), rs.getString("hrcontact")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDrives() {
        driveModel.setRowCount(0);
        try {
            ResultSet rs = DriveDB.getAllDrives();
            if (rs == null)
                return;
            while (rs.next()) {
                driveModel.addRow(new Object[] {
                        rs.getInt("D_id"), rs.getString("cname"),
                        rs.getString("start_date"), rs.getString("end_date"),
                        rs.getInt("availableSeats"), rs.getDouble("lpa"), rs.getDouble("mingpa")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadApplications(String statusFilter) {
        appModel.setRowCount(0);
        try {
            ResultSet rs = ApplicationDB.getAllApplications(statusFilter);
            if (rs == null)
                return;
            while (rs.next()) {
                appModel.addRow(new Object[] {
                        rs.getInt("A_id"), rs.getInt("driveId"), rs.getString("cname"),
                        rs.getString("s_id"), rs.getString("sname"),
                        rs.getString("applicationDate"), rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadCompanyCombo(JComboBox<String> cb) {
        try {
            ResultSet rs = CompanyDB.getCompanyIdNames();
            if (rs == null)
                return;
            while (rs.next())
                cb.addItem(rs.getInt("id") + " - " + rs.getString("name"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ==================== Helpers ====================
    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean confirm(String m) {
        return JOptionPane.showConfirmDialog(this, m, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
