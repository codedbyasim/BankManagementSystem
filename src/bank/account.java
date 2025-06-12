package bank;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class account extends javax.swing.JInternalFrame {

    Connection con1;
    PreparedStatement insert;
    ResultSet rs1;

    private JLabel lblAccId, lblCustId, lblCustName, lblAccType, lblBalance;
    private JTextField txtCustId, txtCustName, txtBalance;
    private JComboBox<String> cbAccType;
    private JButton btnAdd, btnCancel;
    private JLabel accIdDisplay;

    public account() {
        initLookAndFeel();
        initComponents();
        autoId();
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    private void initComponents() {
        setClosable(true);
        setTitle("Create New Account");
        setSize(500, 400);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 420, 300);
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder("Account Registration"));
        panel.setLayout(null);
        add(panel);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        lblAccId = new JLabel("Account No:");
        lblAccId.setBounds(30, 30, 120, 25);
        lblAccId.setFont(labelFont);
        panel.add(lblAccId);

        accIdDisplay = new JLabel("A0001");
        accIdDisplay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accIdDisplay.setForeground(new Color(0, 102, 204));
        accIdDisplay.setBounds(160, 30, 200, 25);
        panel.add(accIdDisplay);

        lblCustId = new JLabel("Customer ID:");
        lblCustId.setBounds(30, 70, 120, 25);
        lblCustId.setFont(labelFont);
        panel.add(lblCustId);

        txtCustId = new JTextField();
        txtCustId.setFont(fieldFont);
        txtCustId.setBounds(160, 70, 200, 25);
        txtCustId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcustKeyPressed(evt);
            }
        });
        panel.add(txtCustId);

        lblCustName = new JLabel("Customer Name:");
        lblCustName.setBounds(30, 110, 120, 25);
        lblCustName.setFont(labelFont);
        panel.add(lblCustName);

        txtCustName = new JTextField();
        txtCustName.setFont(fieldFont);
        txtCustName.setEditable(false);
        txtCustName.setBounds(160, 110, 200, 25);
        panel.add(txtCustName);

        lblAccType = new JLabel("Account Type:");
        lblAccType.setBounds(30, 150, 120, 25);
        lblAccType.setFont(labelFont);
        panel.add(lblAccType);

        cbAccType = new JComboBox<>(new String[]{"Saving", "Fix", "Current"});
        cbAccType.setBounds(160, 150, 200, 25);
        cbAccType.setFont(fieldFont);
        panel.add(cbAccType);

        lblBalance = new JLabel("Balance:");
        lblBalance.setBounds(30, 190, 120, 25);
        lblBalance.setFont(labelFont);
        panel.add(lblBalance);

        txtBalance = new JTextField();
        txtBalance.setFont(fieldFont);
        txtBalance.setBounds(160, 190, 200, 25);
        panel.add(txtBalance);

        btnAdd = new JButton("Add");
        btnAdd.setFont(labelFont);
        btnAdd.setBounds(160, 240, 90, 35);
        btnAdd.setBackground(new Color(0, 123, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addAccount());
        panel.add(btnAdd);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(labelFont);
        btnCancel.setBounds(270, 240, 90, 35);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);
    }

    private void autoId() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            Statement s = con1.createStatement();
            ResultSet rs = s.executeQuery("SELECT MAX(acc_id) FROM account");
            rs.next();

            if (rs.getString(1) == null) {
                accIdDisplay.setText("A0001");
            } else {
                long id = Long.parseLong(rs.getString(1).substring(2));
                id++;
                accIdDisplay.setText("A0" + String.format("%03d", id));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void txtcustKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                String custId = txtCustId.getText();
                Class.forName("com.mysql.jdbc.Driver");
                con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
                insert = con1.prepareStatement("SELECT * FROM customer WHERE cust_id = ?");
                insert.setString(1, custId);
                rs1 = insert.executeQuery();

                if (!rs1.next()) {
                    JOptionPane.showMessageDialog(null, "Customer ID not found.");
                } else {
                    txtCustName.setText(rs1.getString("firstname").trim());
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(account.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addAccount() {
    String id = accIdDisplay.getText();
    String custId = txtCustId.getText().trim();
    String accType = cbAccType.getSelectedItem().toString();
    String balance = txtBalance.getText().trim();

    if (custId.isEmpty() || balance.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double balVal = Double.parseDouble(balance);
        if (balVal < 0) {
            JOptionPane.showMessageDialog(this, "Balance cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");

        // Check if customer exists
        insert = con1.prepareStatement("SELECT * FROM customer WHERE cust_id = ?");
        insert.setString(1, custId);
        rs1 = insert.executeQuery();

        if (!rs1.next()) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID. No such customer found.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert new account
        insert = con1.prepareStatement("INSERT INTO account(acc_id, cust_id, acc_type, balance) VALUES (?, ?, ?, ?)");
        insert.setString(1, id);
        insert.setString(2, custId);
        insert.setString(3, accType);
        insert.setDouble(4, balVal);

        int result = insert.executeUpdate();

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Account Created Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
            autoId();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number for balance.", "Format Error", JOptionPane.ERROR_MESSAGE);
    } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException fkError) {
        JOptionPane.showMessageDialog(this, "Database Error:\nForeign key constraint violated. Make sure the Customer ID exists.", "Database Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        try { if (rs1 != null) rs1.close(); } catch (Exception e) {}
        try { if (insert != null) insert.close(); } catch (Exception e) {}
        try { if (con1 != null) con1.close(); } catch (Exception e) {}
    }
}


    private void resetFields() {
        txtCustId.setText("");
        txtCustName.setText("");
        cbAccType.setSelectedIndex(0);
        txtBalance.setText("");
    }
    
 // Logic extracted for testing
    public String generateNextAccountId(String lastId) {
        if (lastId == null) return "A0001";

        long id = Long.parseLong(lastId.substring(1));
        id++;
        return "A" + String.format("%04d", id);
    }

    public String getCustomerNameById(String custId) {
        String name = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("SELECT * FROM customer WHERE cust_id = ?");
            insert.setString(1, custId);
            rs1 = insert.executeQuery();

            if (rs1.next()) {
                name = rs1.getString("firstname").trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs1 != null) rs1.close(); } catch (Exception e) {}
            try { if (insert != null) insert.close(); } catch (Exception e) {}
            try { if (con1 != null) con1.close(); } catch (Exception e) {}
        }
        return name;
    }
    
}