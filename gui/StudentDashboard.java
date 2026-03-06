package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import DB_connections.*;

public class StudentDashboard extends JFrame {

    private String rollNum;
    private JPanel contentPanel;

    public StudentDashboard(String rollNum, String name) {
        this.rollNum = rollNum;
        setTitle("Welcome, " + name);
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
                    0, 0, new Color(46, 184, 92),
                    getWidth(), getHeight(), new Color(76, 209, 138)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle pattern texture
                g2d.setColor(new Color(255, 255, 255, 8));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        g2d.drawLine(i, j, i + 10, j + 10);
                    }
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Menu buttons at the top with colors
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBackground(new Color(30, 140, 60));
        JButton profileBtn = new JButton("My Profile");
        styleNavButton(profileBtn, new Color(76, 175, 80));
        JButton drivesBtn = new JButton("Available Drives");
        styleNavButton(drivesBtn, new Color(76, 175, 80));
        JButton appsBtn = new JButton("My Applications");
        styleNavButton(appsBtn, new Color(76, 175, 80));
        JButton logoutBtn = new JButton("Logout");
        styleNavButton(logoutBtn, new Color(220, 20, 60));
        menuPanel.add(profileBtn);
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

        profileBtn.addActionListener(e -> showProfile());
        drivesBtn.addActionListener(e -> showDrives());
        appsBtn.addActionListener(e -> showApplications());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        mainPanel.add(menuPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        showProfile();
        setVisible(true);
    }

    private void styleNavButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createRoundedPanel(Color bgColor, int radius) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                // Add shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
        };
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(new Color(40, 40, 40));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 220, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    // ---- Profile Section ----
    private void showProfile() {
        // Create centered card panel
        JPanel cardPanel = createRoundedPanel(new Color(255, 255, 255), 20);
        cardPanel.setLayout(new GridLayout(6, 2, 15, 15));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setPreferredSize(new Dimension(500, 400));

        JTextField fRoll = new JTextField(rollNum);
        fRoll.setEditable(false);
        fRoll.setBackground(new Color(240, 240, 240));
        styleTextField(fRoll);
        
        JTextField fName = new JTextField();
        styleTextField(fName);
        
        JTextField fAge = new JTextField();
        styleTextField(fAge);
        
        JTextField fMajor = new JTextField();
        styleTextField(fMajor);
        
        JTextField fGPA = new JTextField();
        styleTextField(fGPA);

        // load current data from DB
        try {
            ResultSet rs = StudentDB.getStudentByRoll(rollNum);
            if (rs != null && rs.next()) {
                fName.setText(rs.getString("name"));
                fAge.setText(String.valueOf(rs.getInt("age")));
                fMajor.setText(rs.getString("major"));
                fGPA.setText(String.valueOf(rs.getDouble("gpa")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        cardPanel.add(createStyledLabel("Roll No:"));
        cardPanel.add(fRoll);
        cardPanel.add(createStyledLabel("Name:"));
        cardPanel.add(fName);
        cardPanel.add(createStyledLabel("Age:"));
        cardPanel.add(fAge);
        cardPanel.add(createStyledLabel("Major:"));
        cardPanel.add(fMajor);
        cardPanel.add(createStyledLabel("GPA:"));
        cardPanel.add(fGPA);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveBtn.addActionListener(e -> {
            try {
                StudentDB.updateStudent(rollNum, fName.getText().trim(),
                        Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                        Double.parseDouble(fGPA.getText().trim()));
                JOptionPane.showMessageDialog(this, "Profile updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be integer, GPA must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cardPanel.add(saveBtn);
        cardPanel.add(new JLabel());

        // Create wrapper panel for centering
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapperPanel.add(cardPanel, gbc);

        contentPanel.removeAll();
        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---- Available Drives Section ----
    private void showDrives() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Drive ID", "Company", "Start", "End", "Seats", "LPA", "Min GPA" }, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(76, 175, 80));
        table.getTableHeader().setForeground(Color.WHITE);

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
        applyBtn.setFont(new Font("Arial", Font.BOLD, 12));
        applyBtn.setBackground(new Color(255, 140, 0));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setFocusPainted(false);
        applyBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        applyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a drive first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int driveId = (int) model.getValueAt(row, 0);
            double minGpa = (double) model.getValueAt(row, 6);

            // check GPA
            try {
                ResultSet rs = StudentDB.getStudentByRoll(rollNum);
                if (rs != null && rs.next() && rs.getDouble("gpa") < minGpa) {
                    JOptionPane.showMessageDialog(this, "Your GPA is below the minimum required (" + minGpa + ").", "Ineligible", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check duplicate
            if (ApplicationDB.hasApplied(rollNum, driveId)) {
                JOptionPane.showMessageDialog(this, "You already applied to this drive.", "Duplicate", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ApplicationDB.addApplication(driveId, rollNum)) {
                JOptionPane.showMessageDialog(this, "Applied successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to apply.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bp.setOpaque(false);
        bp.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
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
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(76, 175, 80));
        table.getTableHeader().setForeground(Color.WHITE);

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
        withdrawBtn.setFont(new Font("Arial", Font.BOLD, 12));
        withdrawBtn.setBackground(new Color(255, 69, 0));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFocusPainted(false);
        withdrawBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        withdrawBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        withdrawBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an application first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!"Applied".equals(model.getValueAt(row, 4))) {
                JOptionPane.showMessageDialog(this, "Can only withdraw 'Applied' applications.", "Invalid Action", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ApplicationDB.deleteApplication((int) model.getValueAt(row, 0));
            JOptionPane.showMessageDialog(this, "Application withdrawn.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showApplications();
        });

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bp.setOpaque(false);
        bp.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
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
