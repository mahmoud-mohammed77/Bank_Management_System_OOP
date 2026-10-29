package ui;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AccountPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField accountNumberField, balanceField;
    private JComboBox<String> customerCombo, typeCombo;
    private JButton addBtn, updateBalanceBtn, deleteBtn, refreshBtn;

    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private ArrayList<Customer> customersList;
    private int selectedAccountNumber = -1; // عشان نعرف بنعدل مين

    public AccountPanel() {
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        // ================= الجزء العلوي: الجدول =================
        String[] columns = {"model.Account ID", "model.Account Number", "model.Customer Name", "Type", "Balance"};
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

        // ================= الجزء السفلي: الفورم والأزرار =================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("model.Account Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // model.Account Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("model.Account Number:"), gbc);
        accountNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(accountNumberField, gbc);

        // model.Customer (ComboBox)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("model.Customer:"), gbc);
        customerCombo = new JComboBox<>();
        loadCustomersToCombo();
        gbc.gridx = 1;
        formPanel.add(customerCombo, gbc);

        // model.Account Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("model.Account Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"Saving", "Current"});
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        // Balance
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Initial Balance:"), gbc);
        balanceField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(balanceField, gbc);

        // الأزرار
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        addBtn = createStyledButton("Create model.Account", new Color(25, 135, 84));
        updateBalanceBtn = createStyledButton("Update Balance", new Color(13, 110, 253));
        deleteBtn = createStyledButton("Delete model.Account", new Color(220, 53, 69));
        refreshBtn = createStyledButton("Refresh", new Color(108, 117, 125));

        btnPanel.add(addBtn);
        btnPanel.add(updateBalanceBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // ================= تحميل البيانات الأولية =================
        loadAccounts();

        // ================= الأحداث (Events) =================

        // 1. اختيار حساب من الجدول
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedAccountNumber = (int) tableModel.getValueAt(selectedRow, 1);
                    accountNumberField.setText(String.valueOf(selectedAccountNumber));
                    balanceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
                    // ملاحظة: مش بنغير الـ ComboBox بتاع العميل هنا لأن الحساب ملوش علاقة بالتعديل المباشر
                }
            }
        });

        // 2. زرار إنشاء حساب
        addBtn.addActionListener(e -> {
            if (customerCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please add a customer first!");
                return;
            }

            try {
                int accNumber = Integer.parseInt(accountNumberField.getText());
                double balance = Double.parseDouble(balanceField.getText());

                // بنجيب الـ model.Customer ID من الـ ComboBox
                Customer selectedCustomer = customersList.get(customerCombo.getSelectedIndex());

                // نوع الحساب
                AccountType type = typeCombo.getSelectedItem().equals("Saving") ? AccountType.SAVING : AccountType.CURRENT;

                Account account;
                if (type == AccountType.SAVING) {
                    account = new SavingAccount(accNumber, selectedCustomer, balance);
                } else {
                    account = new CurrentAccount(accNumber, selectedCustomer, balance);
                }

                accountDAO.addAccount(account);
                clearFields();
                loadAccounts();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Number Format!");
            }
        });

        // 3. زرار تحديث الرصيد
        updateBalanceBtn.addActionListener(e -> {
            if (selectedAccountNumber == -1) {
                JOptionPane.showMessageDialog(this, "Please select an account from the table first.");
                return;
            }
            try {
                double newBalance = Double.parseDouble(balanceField.getText());
                accountDAO.updateBalance(selectedAccountNumber, newBalance);
                loadAccounts();
                selectedAccountNumber = -1;
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Balance Format!");
            }
        });

        // 4. زرار الحذف
        deleteBtn.addActionListener(e -> {
            if (selectedAccountNumber == -1) {
                JOptionPane.showMessageDialog(this, "Please select an account from the table first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this account?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                accountDAO.DeleteAccount(selectedAccountNumber);
                clearFields();
                loadAccounts();
                selectedAccountNumber = -1;
            }
        });

        // 5. زرار التحديث
        refreshBtn.addActionListener(e -> {
            loadCustomersToCombo();
            loadAccounts();
        });
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

    // Method عشان نملأ الـ ComboBox بالعملاء
    private void loadCustomersToCombo() {
        customerCombo.removeAllItems();
        customersList = customerDAO.getAllCustomer();
        for (Customer c : customersList) {
            customerCombo.addItem(c.getFullName() + " (ID: " + c.getId() + ")");
        }
    }

    // Method عشان نجيب البيانات من الداتابيز ونحطها في الجدول
    private void loadAccounts() {
        tableModel.setRowCount(0);
        ArrayList<Account> accounts = accountDAO.getAllAccounts();

        for (Account a : accounts) {
            Object[] row = {
                    a.getAccountID(),
                    a.getAccountNumber(),
                    a.getcostomer().getFullName(),
                    a.getAccountType(),
                    a.getBalance()
            };
            tableModel.addRow(row);
        }
    }

    // Method عشان ننضف الخانات
    private void clearFields() {
        accountNumberField.setText("");
        balanceField.setText("");
    }
}