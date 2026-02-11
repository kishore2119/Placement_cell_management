package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import DB_connections.*;

public class StudentDashboard extends JFrame {

    private String rollNum;
    private JPanel contentPanel;

    public StudentDashboard(String rollNum, String name) {
        this.rollNum = rollNum;
        setTitle("Welcome, " + name);
        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu buttons at the top
        JPanel menuPanel = new JPanel(new FlowLayout());
        JButton profileBtn = new JButton("My Profile");
        JButton drivesBtn = new JButton("Available Drives");
        JButton appsBtn = new JButton("My Applications");
        JButton logoutBtn = new JButton("Logout");
        menuPanel.add(profileBtn);
        menuPanel.add(drivesBtn);
        menuPanel.add(appsBtn);
        menuPanel.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout());

        profileBtn.addActionListener(e -> showProfile());
        drivesBtn.addActionListener(e -> showDrives());
        appsBtn.addActionListener(e -> showApplications());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(menuPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        showProfile();
        setVisible(true);
    }

    // ---- Profile Section ----
    private void showProfile() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField fRoll = new JTextField(rollNum);
        fRoll.setEditable(false);
        JTextField fName = new JTextField();
        JTextField fAge = new JTextField();
        JTextField fMajor = new JTextField();
        JTextField fGPA = new JTextField();
        JTextField fEmail = new JTextField();

        // load current data from DB
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

        panel.add(new JLabel("Roll No:"));
        panel.add(fRoll);
        panel.add(new JLabel("Name:"));
        panel.add(fName);
        panel.add(new JLabel("Age:"));
        panel.add(fAge);
        panel.add(new JLabel("Major:"));
        panel.add(fMajor);
        panel.add(new JLabel("GPA:"));
        panel.add(fGPA);
        panel.add(new JLabel("Email:"));
        panel.add(fEmail);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> {
            try {
                StudentDB.updateStudent(rollNum, fName.getText().trim(),
                        Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                        Double.parseDouble(fGPA.getText().trim()), fEmail.getText().trim());
                msg("Profile updated!");
            } catch (NumberFormatException ex) {
                msg("Age must be integer, GPA must be a number.");
            }
        });
        panel.add(saveBtn);
        panel.add(new JLabel());

        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---- Available Drives Section ----
    private void showDrives() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Drive ID", "Company", "Start", "End", "Seats", "LPA", "Min GPA" }, 0);
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

        JButton applyBtn = new JButton("Apply to Drive");
        applyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select a drive first.");
                return;
            }
            int driveId = (int) model.getValueAt(row, 0);
            double minGpa = (double) model.getValueAt(row, 6);

            // check GPA
            try {
                ResultSet rs = StudentDB.getStudentByRoll(rollNum);
                if (rs != null && rs.next() && rs.getDouble("gpa") < minGpa) {
                    msg("Your GPA is below the minimum required (" + minGpa + ").");
                    return;
                }
            } catch (SQLException ex) {
                msg("Error: " + ex.getMessage());
                return;
            }

            // check duplicate
            if (ApplicationDB.hasApplied(rollNum, driveId)) {
                msg("You already applied to this drive.");
                return;
            }
            if (ApplicationDB.addApplication(driveId, rollNum)) {
                msg("Applied successfully!");
            } else {
                msg("Failed to apply.");
            }
        });

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(applyBtn);

        contentPanel.removeAll();
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPanel.add(bp, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---- My Applications Section ----
    private void showApplications() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "App ID", "Drive", "Company", "Date", "Status" }, 0);
        JTable table = new JTable(model);

        try {
            ResultSet rs = ApplicationDB.getApplicationsByStudent(rollNum);
            while (rs != null && rs.next()) {
                model.addRow(new Object[] { rs.getInt("A_id"), rs.getInt("driveId"),
                        rs.getString("cname"), rs.getString("applicationDate"),
                        rs.getString("status") });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                msg("Select an application first.");
                return;
            }
            if (!"Applied".equals(model.getValueAt(row, 4))) {
                msg("Can only withdraw 'Applied' applications.");
                return;
            }
            ApplicationDB.deleteApplication((int) model.getValueAt(row, 0));
            msg("Application withdrawn.");
            showApplications();
        });

        JPanel bp = new JPanel(new FlowLayout());
        bp.add(withdrawBtn);

        contentPanel.removeAll();
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPanel.add(bp, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}
