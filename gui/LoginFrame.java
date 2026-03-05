package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import DB_connections.StudentDB;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Placement Cell - Login");
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
                
                // Gradient background (Deep Blue to Purple)
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 135, 155),      // Deep teal
                    getWidth(), getHeight(), new Color(70, 180, 200) // Cyan-teal
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle pattern texture
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        g2d.drawLine(i, j, i + 10, j + 10);
                    }
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Create centered white card panel
        JPanel cardPanel = createRoundedPanel(new Color(255, 255, 255), 20);
        cardPanel.setLayout(new GridLayout(7, 2, 12, 12));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        cardPanel.setPreferredSize(new Dimension(380, 320));

        // Add title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Placement Cell");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 135, 155));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage Your Career");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        // Input fields
        JComboBox<String> roleBox = new JComboBox<>(new String[] { "Admin", "Student" });
        styleComboBox(roleBox);
        
        JTextField userField = new JTextField();
        styleTextField(userField, "Emp ID / Roll No");
        
        JPasswordField passField = new JPasswordField();
        stylePasswordField(passField, "Password");
        
        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(46, 184, 92)); // Green
        
        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(52, 152, 219)); // Blue

        // Add components to card
        cardPanel.add(createLabelWithIcon("Role:"));
        cardPanel.add(roleBox);
        cardPanel.add(createLabelWithIcon("ID / Roll:"));
        cardPanel.add(userField);
        cardPanel.add(createLabelWithIcon("Password:"));
        cardPanel.add(passField);
        cardPanel.add(new JLabel());
        cardPanel.add(new JLabel());
        cardPanel.add(loginBtn);
        cardPanel.add(registerBtn);

        // Login button logic
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            if ("Admin".equals(roleBox.getSelectedItem())) {
                // predefined admin credentials (hardcoded list)
                String[][] admins = {
                        {"admin", "admin"},
                        {"alice", "alice123"},
                        {"bob", "bobpass"},
                        {"charlie", "charlie789"},
                        {"diana", "diana!@#"}
                };
                boolean validAdmin = false;
                for (String[] pair : admins) {
                    if (pair[0].equals(user) && pair[1].equals(pass)) {
                        validAdmin = true;
                        break;
                    }
                }
                if (validAdmin) {
                    dispose();
                    new AdminDashboard();
                } else {
                    StringBuilder sb = new StringBuilder("Invalid admin credentials.\n");
                    sb.append("Use one of the following logins:\n");
                    for (String[] pair : admins) {
                        sb.append(pair[0]).append(" / ").append(pair[1]).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString());
                }
            } else {
                try {
                    ResultSet rs = StudentDB.validateStudent(user, pass);
                    if (rs != null && rs.next()) {
                        String name = rs.getString("name");
                        dispose();
                        new StudentDashboard(user, name);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid roll number or password.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Register button logic
        registerBtn.addActionListener(e -> {
            JTextField fRoll = new JTextField(), fName = new JTextField(),
                    fAge = new JTextField(), fMajor = new JTextField(),
                    fGPA = new JTextField();
            JPasswordField fPassword = new JPasswordField(),
                    fConfirmPassword = new JPasswordField();
            Object[] fields = { "Roll No:", fRoll, "Name:", fName, "Age:", fAge,
                    "Major:", fMajor, "GPA:", fGPA, "Password:", fPassword, "Confirm Password:", fConfirmPassword };
            if (JOptionPane.showConfirmDialog(this, fields, "Student Registration",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String password = new String(fPassword.getPassword()).trim();
                    String confirmPassword = new String(fConfirmPassword.getPassword()).trim();
                    
                    if (password.isEmpty() || confirmPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Password fields cannot be empty.");
                        return;
                    }
                    
                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this, "Passwords do not match!");
                        return;
                    }
                    
                    if (StudentDB.addStudent(fRoll.getText().trim(), fName.getText().trim(),
                            Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                            Double.parseDouble(fGPA.getText().trim()), password)) {
                        JOptionPane.showMessageDialog(this, "Registered! You can now login.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be integer, GPA must be a number.");
                }
            }
        });

        // Add title and card to main panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titlePanel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(cardPanel, gbc);

        add(mainPanel);
        setVisible(true);
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
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
        };
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(Color.BLACK);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(Color.BLACK);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 12));
        box.setBackground(new Color(245, 245, 245));
        box.setForeground(Color.BLACK);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createLabelWithIcon(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
