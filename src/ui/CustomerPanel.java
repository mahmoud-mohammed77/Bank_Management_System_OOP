package ui;

import dao.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CustomerPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, emailField;
    private JButton addBtn, updateBtn, deleteBtn, refreshBtn;

    private CustomerDAO customerDAO;
    private int selectedCustomerId = -1; // عشان نعرف بنعدل مين

    public CustomerPanel() {
        customerDAO = new CustomerDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        // ================= الجزء العلوي: الجدول =================
        String[] columns = {"ID", "Full Name", "Phone", "Email", "Created At"};
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
        formPanel.setBorder(BorderFactory.createTitledBorder("model.Customer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // الأزرار
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        addBtn = createStyledButton("Add", new Color(25, 135, 84));
        updateBtn = createStyledButton("Update", new Color(13, 110, 253));
        deleteBtn = createStyledButton("Delete", new Color(220, 53, 69));
        refreshBtn = createStyledButton("Refresh", new Color(108, 117, 125));

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // ================= تحميل البيانات الأولية =================
        loadCustomers();

        // ================= الأحداث (Events) =================

        // 1. اختيار عميل من الجدول
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // بنجيب البيانات من الصف اللي اتداس عليه
                    selectedCustomerId = (int) tableModel.getValueAt(selectedRow, 0);
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    phoneField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    emailField.setText((String) tableModel.getValueAt(selectedRow, 3));
                }
            }
        });

        // 2. زرار الإضافة
        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            Customer customer = new Customer(name, phone, email, java.time.LocalDateTime.now());
            customerDAO.addCustomer(customer);
            clearFields();
            loadCustomers();
        });

        // 3. زرار التعديل
        updateBtn.addActionListener(e -> {
            if (selectedCustomerId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer from the table first.");
                return;
            }

            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            Customer customer = new Customer(selectedCustomerId, name, phone, email, java.time.LocalDateTime.now());
            customerDAO.UpdateAccount(customer);
            clearFields();
            loadCustomers();
            selectedCustomerId = -1;
        });

        // 4. زرار الحذف
        deleteBtn.addActionListener(e -> {
            if (selectedCustomerId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer from the table first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerDAO.DelateCustomer(selectedCustomerId);
                clearFields();
                loadCustomers();
                selectedCustomerId = -1;
            }
        });

        // 5. زرار التحديث
        refreshBtn.addActionListener(e -> loadCustomers());
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

    // Method عشان نجيب البيانات من الداتابيز ونحطها في الجدول
    private void loadCustomers() {
        tableModel.setRowCount(0);
        ArrayList<Customer> customers = customerDAO.getAllCustomer();

        for (Customer c : customers) {
            Object[] row = {
                    c.getId(),
                    c.getFullName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }

    // Method عشان ننضف الخانات بعد الإضافة أو التعديل
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }
}