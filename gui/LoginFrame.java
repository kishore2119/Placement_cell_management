package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import DB_connections.StudentDB;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Placement Cell - Login");
        setSize(350, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> roleBox = new JComboBox<>(new String[] { "Admin", "Student" });
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);
        panel.add(new JLabel("Username / Roll No:"));
        panel.add(userField);
        panel.add(new JLabel("Password / Email:"));
        panel.add(passField);
        panel.add(new JLabel()); // empty cell
        panel.add(new JLabel()); // empty cell
        panel.add(loginBtn);
        panel.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            if ("Admin".equals(roleBox.getSelectedItem())) {
                if ("admin".equals(user) && "admin".equals(pass)) {
                    dispose();
                    new AdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.");
                }
            } else {
                try {
                    ResultSet rs = StudentDB.validateStudent(user, pass);
                    if (rs != null && rs.next()) {
                        String name = rs.getString("name");
                        dispose();
                        new StudentDashboard(user, name);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid roll number or email.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        registerBtn.addActionListener(e -> {
            JTextField fRoll = new JTextField(), fName = new JTextField(),
                    fAge = new JTextField(), fMajor = new JTextField(),
                    fGPA = new JTextField(), fEmail = new JTextField();
            Object[] fields = { "Roll No:", fRoll, "Name:", fName, "Age:", fAge,
                    "Major:", fMajor, "GPA:", fGPA, "Email:", fEmail };
            if (JOptionPane.showConfirmDialog(this, fields, "Student Registration",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    if (StudentDB.addStudent(fRoll.getText().trim(), fName.getText().trim(),
                            Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                            Double.parseDouble(fGPA.getText().trim()), fEmail.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Registered! You can now login.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be integer, GPA must be a number.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
