package ui;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;


public class TransactionPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField accountNumberField;
    private JButton searchBtn, refreshBtn;

    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    public TransactionPanel() {
        transactionDAO = new TransactionDAO();
        accountDAO = new AccountDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        // ================= الجزء العلوي: البحث =================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createTitledBorder("Search Transactions"));

        topPanel.add(new JLabel("Account Number:"));
        accountNumberField = new JTextField(15);
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(accountNumberField);

        searchBtn = createStyledButton("Search", new Color(13, 110, 253));
        refreshBtn = createStyledButton("Show All", new Color(108, 117, 125));

        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        // ================= الجزء الأوسط: الجدول =================
        String[] columns = {"ID", "Account Number", "Type", "Amount", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ================= الأحداث (Events) =================

        // 1. البحث برقم الحساب
        searchBtn.addActionListener(e -> {
            String accNumStr = accountNumberField.getText().trim();
            if (accNumStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Account Number.");
                return;
            }

            try {
                int accNumber = Integer.parseInt(accNumStr);

                Account account = accountDAO.getAccountByNumber(accNumber);
                if (account == null) {
                    JOptionPane.showMessageDialog(this, "Account not found!");
                    return;
                }

                ArrayList<Transaction> transactions = transactionDAO.getTransactionsByAccount(account.getAccountID());
                loadTableData(transactions, accNumber);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Account Number format!");
            }
        });

        // 2. عرض كل العمليات
        refreshBtn.addActionListener(e -> {
            // نمسح الجدول ونعرض رسالة
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Please search by account number to view transactions.");
        });
    }

    // Method عشان نحط البيانات في الجدول
    private void loadTableData(ArrayList<Transaction> transactions, int accountNumber) {
        tableModel.setRowCount(0);
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for this account.");
            return;
        }

        for (Transaction t : transactions) {
            Object[] row = {
                    t.getId(),              // Transaction ID
                    accountNumber,          // Account Number
                    t.getType(),
                    t.getAmount(),
                    t.getData(),
                    t.getStatus()           // Status
            };
            tableModel.addRow(row);
        }
    }

    // Method مساعدة لتنسيق الأزرار
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}