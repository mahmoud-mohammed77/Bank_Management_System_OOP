package ui;

import com.formdev.flatlaf.FlatLightLaf;
import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        // تفعيل شكل FlatLaf الحديث
        FlatLightLaf.setup();

        setTitle("model.Bank Management System - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // البانل الرئيسي
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250)); // لون رمادي فاتح جداً
        mainPanel.setLayout(new GridBagLayout()); // عشان نوسط العناصر في النص
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // مسافات بين العناصر

        // 1. العنوان
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JLabel subTitleLabel = new JLabel("Please sign in to continue");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(108, 117, 125));
        subTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainPanel.add(subTitleLabel, gbc);

        // 2. حقل المستخدم
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 2;
        mainPanel.add(userLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 3;
        mainPanel.add(usernameField, gbc);

        // 3. حقل الباسورد
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 4;
        mainPanel.add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 5;
        mainPanel.add(passwordField, gbc);

        // 4. زرار الدخول
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(13, 110, 253)); // أزرق
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(25, 0, 10, 0);
        mainPanel.add(loginButton, gbc);

        add(mainPanel);

        // 5. حدث تسجيل الدخول
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                EmployeeDAO dao = new EmployeeDAO();
                Employee employee = dao.login(username, password);

                if (employee != null) {
                    // الدخول نجح! هنفتح الشاشة الرئيسية ونقفل شاشة الدخول
                    dispose(); // قفل شاشة الدخول
                    new Dashboard(employee).setVisible(true); // فتح الشاشة الرئيسية
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}