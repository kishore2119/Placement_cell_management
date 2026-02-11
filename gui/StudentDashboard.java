package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import DB_connections.*;

public class StudentDashboard extends JFrame {

    private String rollNum;
    private DefaultTableModel driveModel, appModel;
    private JTextField fName, fAge, fMajor, fGPA, fEmail;

    public StudentDashboard(String rollNum, String studentName) {
        this.rollNum = rollNum;

        setTitle("Student Dashboard - " + studentName);
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profile", buildProfileTab());
        tabs.addTab("Available Drives", buildDrivesTab());
        tabs.addTab("My Applications", buildApplicationsTab());

        // top bar
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel titleLbl = new JLabel("  Welcome, " + studentName);
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

        loadProfile();
        loadDrives();
        loadApplications();
        setVisible(true);
    }

    // ==================== Profile Tab ====================
    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField fRoll = new JTextField(rollNum);
        fRoll.setEditable(false);
        fName = new JTextField(20);
        fAge = new JTextField(20);
        fMajor = new JTextField(20);
        fGPA = new JTextField(20);
        fEmail = new JTextField(20);

        String[] labels = { "Roll No:", "Name:", "Age:", "Major:", "GPA:", "Email:" };
        JTextField[] fields = { fRoll, fName, fAge, fMajor, fGPA, fEmail };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton saveBtn = new JButton("Save");
        btnPanel.add(refreshBtn);
        btnPanel.add(saveBtn);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        refreshBtn.addActionListener(e -> loadProfile());
        saveBtn.addActionListener(e -> saveProfile());

        return panel;
    }

    // ==================== Drives Tab ====================
    private JPanel buildDrivesTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        driveModel = new DefaultTableModel(
                new String[] { "Drive ID", "Company", "Start Date", "End Date", "Seats", "LPA", "Min GPA" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(driveModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton applyBtn = new JButton("Apply");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(applyBtn);
        btnPanel.add(refreshBtn);

        applyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a drive first.");
                return;
            }
            int driveId = (int) driveModel.getValueAt(row, 0);
            double minGpa = (double) driveModel.getValueAt(row, 6);

            // check student GPA
            try {
                ResultSet rs = StudentDB.getStudentByRoll(rollNum);
                if (rs != null && rs.next()) {
                    double myGpa = rs.getDouble("gpa");
                    if (myGpa < minGpa) {
                        msg("Your GPA (" + myGpa + ") is below the minimum (" + minGpa + ").");
                        return;
                    }
                }
            } catch (SQLException ex) {
                msg("Error checking GPA: " + ex.getMessage());
                return;
            }

            // check duplicate
            if (ApplicationDB.hasApplied(rollNum, driveId)) {
                msg("You have already applied to this drive.");
                return;
            }

            if (ApplicationDB.addApplication(driveId, rollNum)) {
                msg("Applied successfully!");
                loadApplications();
            } else {
                msg("Failed to apply.");
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
                new String[] { "App ID", "Drive ID", "Company", "Date", "Status" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(appModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton withdrawBtn = new JButton("Withdraw");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(withdrawBtn);
        btnPanel.add(refreshBtn);

        withdrawBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select an application first.");
                return;
            }
            String status = (String) appModel.getValueAt(row, 4);
            if (!"Applied".equals(status)) {
                msg("Can only withdraw applications with 'Applied' status.");
                return;
            }
            int appId = (int) appModel.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Withdraw this application?",
                    "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (ApplicationDB.deleteApplication(appId)) {
                    msg("Application withdrawn.");
                    loadApplications();
                } else {
                    msg("Failed to withdraw.");
                }
            }
        });

        refreshBtn.addActionListener(e -> loadApplications());

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Data Loading ====================
    private void loadProfile() {
        try {
            ResultSet rs = StudentDB.getStudentByRoll(rollNum);
            if (rs != null && rs.next()) {
                fName.setText(rs.getString("name"));
                fAge.setText(String.valueOf(rs.getInt("age")));
                fMajor.setText(rs.getString("major"));
                fGPA.setText(String.valueOf(rs.getDouble("gpa")));
                fEmail.setText(rs.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void saveProfile() {
        try {
            int age = Integer.parseInt(fAge.getText().trim());
            double gpa = Double.parseDouble(fGPA.getText().trim());
            if (StudentDB.updateStudent(rollNum, fName.getText().trim(), age,
                    fMajor.getText().trim(), gpa, fEmail.getText().trim())) {
                msg("Profile updated.");
            } else {
                msg("Update failed.");
            }
        } catch (NumberFormatException ex) {
            msg("Age must be integer, GPA must be a number.");
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

    private void loadApplications() {
        appModel.setRowCount(0);
        try {
            ResultSet rs = ApplicationDB.getApplicationsByStudent(rollNum);
            if (rs == null)
                return;
            while (rs.next()) {
                appModel.addRow(new Object[] {
                        rs.getInt("A_id"), rs.getInt("driveId"),
                        rs.getString("cname"), rs.getString("applicationDate"),
                        rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}
