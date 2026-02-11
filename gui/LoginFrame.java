package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import DB_connections.StudentDB;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("Placement Cell - Login");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Placement Cell Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(title, gbc);

        // Role
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[] { "Admin", "Student" });
        panel.add(roleCombo, gbc);

        // Username
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Username/Roll:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password/Email:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Error label
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel, gbc);

        // Login button
        gbc.gridy = 5;
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
        panel.add(loginBtn, gbc);

        // Register link for students
        gbc.gridy = 6;
        JButton registerBtn = new JButton("New Student? Register here");
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setForeground(Color.BLUE);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> openRegisterDialog());
        panel.add(registerBtn, gbc);

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please fill all fields.");
            return;
        }

        if ("Admin".equals(role)) {
            if ("admin".equals(user) && "admin".equals(pass)) {
                dispose();
                new AdminDashboard();
            } else {
                errorLabel.setText("Invalid admin credentials.");
            }
        } else {
            loginStudent(user, pass);
        }
    }

    private void loginStudent(String rollNum, String email) {
        try {
            ResultSet rs = StudentDB.validateStudent(rollNum, email);
            if (rs != null && rs.next()) {
                String name = rs.getString("name");
                dispose();
                new StudentDashboard(rollNum, name);
            } else {
                errorLabel.setText("Invalid roll number or email.");
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void openRegisterDialog() {
        JTextField fRoll = new JTextField();
        JTextField fName = new JTextField();
        JTextField fAge = new JTextField();
        JTextField fMajor = new JTextField();
        JTextField fGPA = new JTextField();
        JTextField fEmail = new JTextField();

        Object[] fields = {
                "Roll No:", fRoll, "Name:", fName, "Age:", fAge,
                "Major:", fMajor, "GPA:", fGPA, "Email:", fEmail
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Student Registration",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String roll = fRoll.getText().trim();
            String name = fName.getText().trim();
            String ageStr = fAge.getText().trim();
            String major = fMajor.getText().trim();
            String gpaStr = fGPA.getText().trim();
            String email = fEmail.getText().trim();

            if (roll.isEmpty() || name.isEmpty() || ageStr.isEmpty()
                    || major.isEmpty() || gpaStr.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (StudentDB.rollExists(roll)) {
                JOptionPane.showMessageDialog(this, "Roll number already exists.");
                return;
            }
            if (StudentDB.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email already registered.");
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                double gpa = Double.parseDouble(gpaStr);
                if (StudentDB.addStudent(roll, name, age, major, gpa, email)) {
                    JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be integer, GPA must be a number.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
