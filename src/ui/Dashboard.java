package ui;

import model.Bank;
import model.Employee;
import ui.AccountPanel;
import ui.CustomerPanel;
import ui.LoginForm;
import ui.TransactionPanel;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private Employee currentEmployee;
    private Bank bank;

    public Dashboard(Employee employee) {
        this.currentEmployee = employee;
        this.bank = new Bank(); // بنعمل Object من البنك عشان نستخدمه في العمليات

        setTitle("Bank Management System - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. البانل الرئيسي
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // 2. الشريط العلوي
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(33, 37, 41));
        topBar.setPreferredSize(new Dimension(1000, 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + employee.getFullName() + " (" + employee.getRole() + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });

        topBar.add(welcomeLabel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        // 3. منطقة المحتوى (شبكة 3x3)
        JPanel contentPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // الأزرار
        JButton btnCustomers = createStyledButton("Manage Customers", new Color(13, 110, 253));
        JButton btnAccounts = createStyledButton("Manage Accounts", new Color(25, 135, 84));
        JButton btnTransactions = createStyledButton("Transactions History", new Color(255, 193, 7), Color.BLACK);

        JButton btnDeposit = createStyledButton("Deposit", new Color(13, 110, 253));
        JButton btnWithdraw = createStyledButton("Withdraw", new Color(220, 53, 69));
        JButton btnTransfer = createStyledButton("Transfer", new Color(255, 193, 7), Color.BLACK);

        JButton btnReports = createStyledButton("Reports", new Color(108, 117, 125));
        JButton btnLogout = createStyledButton("Logout", new Color(33, 37, 41));

        // إضافة الأزرار للشبكة
        contentPanel.add(btnCustomers);
        contentPanel.add(btnAccounts);
        contentPanel.add(btnTransactions);
        contentPanel.add(btnDeposit);
        contentPanel.add(btnWithdraw);
        contentPanel.add(btnTransfer);
        contentPanel.add(btnReports);
        contentPanel.add(btnLogout);
        contentPanel.add(new JLabel("")); // مساحة فاضية لتكميل الشبكة

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // ========== ربط الأزرار بالشاشات ==========

        // 1. شاشة العملاء
        btnCustomers.addActionListener(e -> {
            JFrame customerFrame = new JFrame("Customer Management");
            customerFrame.setSize(800, 600);
            customerFrame.setLocationRelativeTo(null);
            customerFrame.add(new CustomerPanel());
            customerFrame.setVisible(true);
        });

        // 2. شاشة الحسابات
        btnAccounts.addActionListener(e -> {
            JFrame accountFrame = new JFrame("Account Management");
            accountFrame.setSize(850, 650);
            accountFrame.setLocationRelativeTo(null);
            accountFrame.add(new AccountPanel());
            accountFrame.setVisible(true);
        });

        // 3. شاشة العمليات
        btnTransactions.addActionListener(e -> {
            JFrame transFrame = new JFrame("Transactions History");
            transFrame.setSize(850, 600);
            transFrame.setLocationRelativeTo(null);
            transFrame.add(new TransactionPanel());
            transFrame.setVisible(true);
        });

        // 4. الإيداع
        btnDeposit.addActionListener(e -> {
            try {
                String accStr = JOptionPane.showInputDialog(this, "Enter Account Number:");
                if (accStr == null || accStr.isEmpty()) return;
                String amountStr = JOptionPane.showInputDialog(this, "Enter Amount to Deposit:");
                if (amountStr == null || amountStr.isEmpty()) return;

                int accNum = Integer.parseInt(accStr);
                double amount = Double.parseDouble(amountStr);

                if (bank.deposit(accNum, amount)) {
                    JOptionPane.showMessageDialog(this, "Deposit Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit Failed (Account not found)");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!");
            }
        });

        // 5. السحب
        btnWithdraw.addActionListener(e -> {
            try {
                String accStr = JOptionPane.showInputDialog(this, "Enter Account Number:");
                if (accStr == null || accStr.isEmpty()) return;
                String amountStr = JOptionPane.showInputDialog(this, "Enter Amount to Withdraw:");
                if (amountStr == null || amountStr.isEmpty()) return;

                int accNum = Integer.parseInt(accStr);
                double amount = Double.parseDouble(amountStr);

                if (bank.withdraw(accNum, amount)) {
                    JOptionPane.showMessageDialog(this, "Withdraw Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Withdraw Failed (Insufficient balance or account not found)");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!");
            }
        });

        // 6. التحويل
        btnTransfer.addActionListener(e -> {
            try {
                String fromStr = JOptionPane.showInputDialog(this, "From Account Number:");
                if (fromStr == null || fromStr.isEmpty()) return;
                String toStr = JOptionPane.showInputDialog(this, "To Account Number:");
                if (toStr == null || toStr.isEmpty()) return;
                String amountStr = JOptionPane.showInputDialog(this, "Enter Amount to Transfer:");
                if (amountStr == null || amountStr.isEmpty()) return;

                int fromAcc = Integer.parseInt(fromStr);
                int toAcc = Integer.parseInt(toStr);
                double amount = Double.parseDouble(amountStr);

                if (bank.transfer(fromAcc, toAcc, amount)) {
                    JOptionPane.showMessageDialog(this, "Transfer Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Transfer Failed (Check account numbers or balance)");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!");
            }
        });
    }

    // Method مساعدة لتنسيق الأزرار
    private JButton createStyledButton(String text, Color bgColor) {
        return createStyledButton(text, bgColor, Color.WHITE);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}