package bank;

import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class transfer extends javax.swing.JInternalFrame {

    private Connection con;
    private JTextField txtFromAcc, txtBalance, txtToAcc, txtAmount;
    private JButton btnFind, btnTransfer, btnCancel;

    public transfer() {
        FlatIntelliJLaf.setup();
        
        // Remove round corners for buttons
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Panel.background", Color.decode("#f0f2f5"));

        initComponents();
    }

    private void initComponents() {
        setTitle("Transfer Funds");
        setBorder(null);
        setClosable(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 420));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblFromAcc = new JLabel("From Account No:");
        JLabel lblBalance = new JLabel("Current Balance:");
        JLabel lblToAcc = new JLabel("To Account No:");
        JLabel lblAmount = new JLabel("Transfer Amount:");

        lblFromAcc.setFont(font);
        lblBalance.setFont(font);
        lblToAcc.setFont(font);
        lblAmount.setFont(font);

        txtFromAcc = new JTextField(20);
        txtBalance = new JTextField(20);
        txtBalance.setEditable(false);
        txtToAcc = new JTextField(20);
        txtAmount = new JTextField(20);

        txtFromAcc.setFont(font);
        txtBalance.setFont(font);
        txtToAcc.setFont(font);
        txtAmount.setFont(font);

        // Buttons
        btnFind = new JButton("Find");
        btnTransfer = new JButton("Transfer");
        btnCancel = new JButton("Cancel");

        btnFind.setFont(font);
        btnTransfer.setFont(font);
        btnCancel.setFont(font);

        // Set button colors and square corners
        btnFind.setBackground(new Color(0, 123, 255));       // Blue
        btnTransfer.setBackground(new Color(0, 123, 255));   // Blue
        btnCancel.setBackground(new Color(220, 53, 69));     // Red

        btnFind.setForeground(Color.WHITE);
        btnTransfer.setForeground(Color.WHITE);
        btnCancel.setForeground(Color.WHITE);

        btnFind.setFocusPainted(false);
        btnTransfer.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; card.add(lblFromAcc, gbc);
        gbc.gridx = 1; card.add(txtFromAcc, gbc);
        gbc.gridx = 2; card.add(btnFind, gbc);

        gbc.gridx = 0; gbc.gridy = 1; card.add(lblBalance, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(txtBalance, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; card.add(lblToAcc, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(txtToAcc, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; card.add(lblAmount, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(txtAmount, gbc); gbc.gridwidth = 1;

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnTransfer);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        card.add(btnPanel, gbc);

        add(card, BorderLayout.CENTER);

        btnFind.addActionListener(this::findAccount);
        btnTransfer.addActionListener(this::transferAmount);
        btnCancel.addActionListener(e -> dispose());

        pack();
    }

    private void findAccount(ActionEvent evt) {
        String accNo = txtFromAcc.getText().trim();
        if (accNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter From Account No", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            PreparedStatement pst = con.prepareStatement("SELECT balance FROM account WHERE acc_id = ?");
            pst.setString(1, accNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtBalance.setText(rs.getString(1));
            } else {
                JOptionPane.showMessageDialog(this, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
                txtBalance.setText("");
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching account data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transferAmount(ActionEvent evt) {
        String fromAcc = txtFromAcc.getText().trim();
        String toAcc = txtToAcc.getText().trim();
        String amtStr = txtAmount.getText().trim();
        String balanceStr = txtBalance.getText().trim();

        if (fromAcc.isEmpty() || toAcc.isEmpty() || amtStr.isEmpty() || balanceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int amount;
        double currentBalance;

        try {
            amount = Integer.parseInt(amtStr);
            currentBalance = Double.parseDouble(balanceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Transfer amount must be greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount > currentBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient balance for transfer.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean success = performTransfer(fromAcc, toAcc, amount);
            if (success) {
                JOptionPane.showMessageDialog(this, "Transfer successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed. Check account details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Transfer failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean performTransfer(String fromAcc, String toAcc, int amount) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
        con.setAutoCommit(false);

        try {
            PreparedStatement st1 = con.prepareStatement("UPDATE account SET balance = balance - ? WHERE acc_id = ?");
            st1.setInt(1, amount);
            st1.setString(2, fromAcc);
            int rows1 = st1.executeUpdate();

            PreparedStatement st2 = con.prepareStatement("UPDATE account SET balance = balance + ? WHERE acc_id = ?");
            st2.setInt(1, amount);
            st2.setString(2, toAcc);
            int rows2 = st2.executeUpdate();

            if (rows1 == 0 || rows2 == 0) {
                con.rollback();
                return false;
            }

            PreparedStatement st3 = con.prepareStatement("INSERT INTO transfer(f_account, to_account, amount) VALUES (?, ?, ?)");
            st3.setString(1, fromAcc);
            st3.setString(2, toAcc);
            st3.setInt(3, amount);
            st3.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    private void clearFields() {
        txtFromAcc.setText("");
        txtBalance.setText("");
        txtToAcc.setText("");
        txtAmount.setText("");
    }
}
