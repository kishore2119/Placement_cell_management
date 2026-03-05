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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(108, 92, 231),
                    getWidth(), getHeight(), new Color(162, 155, 254)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Menu buttons at the top with colors
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBackground(new Color(70, 60, 180));
        JButton studentsBtn = new JButton("Students");
        styleNavButton(studentsBtn, new Color(100, 200, 200));
        JButton companiesBtn = new JButton("Companies");
        styleNavButton(companiesBtn, new Color(100, 200, 200));
        JButton drivesBtn = new JButton("Drives");
        styleNavButton(drivesBtn, new Color(100, 200, 200));
        JButton appsBtn = new JButton("Applications");
        styleNavButton(appsBtn, new Color(100, 200, 200));
        JButton logoutBtn = new JButton("Logout");
        styleNavButton(logoutBtn, new Color(220, 100, 100));
        menuPanel.add(studentsBtn);
        menuPanel.add(companiesBtn);
        menuPanel.add(drivesBtn);
        menuPanel.add(appsBtn);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setOpaque(false);

        studentsBtn.addActionListener(e -> showStudents());
        companiesBtn.addActionListener(e -> showCompanies());
        drivesBtn.addActionListener(e -> showDrives());
        appsBtn.addActionListener(e -> showApplications());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        mainPanel.add(menuPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
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
        deleteBtn.setBackground(new Color(220, 20, 60)); // Crimson
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(70, 130, 180)); // Steel blue
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);

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
        addBtn.setBackground(new Color(50, 205, 50)); // Lime green
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        JButton editBtn = new JButton("Edit");
        editBtn.setBackground(new Color(255, 215, 0)); // Gold
        editBtn.setForeground(Color.BLACK);
        editBtn.setFocusPainted(false);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 20, 60)); // Crimson
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);

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
        addBtn.setBackground(new Color(50, 205, 50)); // Lime green
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 20, 60)); // Crimson
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);

        addBtn.addActionListener(e -> {
            JComboBox<String> cb = new JComboBox<>();
            try {
                ResultSet rs = CompanyDB.getCompanyIdNames();
                while (rs != null && rs.next())
                    cb.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (cb.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No companies registered yet.\nPlease add a company before creating a drive.",
                        "No Companies", JOptionPane.WARNING_MESSAGE);
                return; // abort creation
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
        filterBtn.setBackground(new Color(70, 130, 180)); // Steel blue
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        JButton statusBtn = new JButton("Update Status");
        statusBtn.setBackground(new Color(255, 140, 0)); // Dark orange
        statusBtn.setForeground(Color.WHITE);
        statusBtn.setFocusPainted(false);

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

    private void styleNavButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}
