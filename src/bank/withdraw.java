package bank;

import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class withdraw extends javax.swing.JInternalFrame {
    Connection con1;
    PreparedStatement insert, insert2;
    ResultSet rs1;
    
    public JTextField txtaccno;
    private JTextField txtfname;
    private JTextField txtlame;
    public JTextField amount;
    private JLabel jLabel6, jLabel7, lbal;
    
    public withdraw() {
        FlatIntelliJLaf.setup(); // Use FlatLaf theme
        
        // Remove rounded corners for buttons (square edges)
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Panel.background", Color.decode("#f0f2f5"));
        
        initComponents();
        setFrameStyle();
        date();
    }
    
    private void setFrameStyle() {
        setTitle("Withdraw Amount");
        setClosable(true);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel lblTitle = new JLabel("Withdraw Funds");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        // Account No:
        JLabel lblAccNo = new JLabel("Account No:");
        lblAccNo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtaccno = new JTextField(15);
        txtaccno.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnFind = new JButton("Find");
        btnFind.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnFind.setBackground(new Color(0, 102, 204)); // Blue
        btnFind.setForeground(Color.WHITE);
        btnFind.setFocusPainted(false);
        btnFind.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        btnFind.setPreferredSize(new Dimension(80, 30));  // Bigger size same as Withdraw
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(lblAccNo, gbc);
        gbc.gridx = 1;
        contentPanel.add(txtaccno, gbc);
        gbc.gridx = 2;
        contentPanel.add(btnFind, gbc);
        
        // Customer ID:
        JLabel lblCustId = new JLabel("Customer ID:");
        lblCustId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel7 = new JLabel("-");
        jLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(lblCustId, gbc);
        gbc.gridx = 1;
        contentPanel.add(jLabel7, gbc);
        
        // First Name:
        JLabel lblFName = new JLabel("First Name:");
        lblFName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtfname = new JTextField(15);
        txtfname.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(lblFName, gbc);
        gbc.gridx = 1;
        contentPanel.add(txtfname, gbc);
        
        // Last Name:
        JLabel lblLName = new JLabel("Last Name:");
        lblLName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtlame = new JTextField(15);
        txtlame.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(lblLName, gbc);
        gbc.gridx = 1;
        contentPanel.add(txtlame, gbc);
        
        // Date:
        JLabel lblDate = new JLabel("Date:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel6 = new JLabel("-");
        jLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        contentPanel.add(jLabel6, gbc);
        
        // Current Balance:
        JLabel lblBalance = new JLabel("Current Balance:");
        lblBalance.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbal = new JLabel("-");
        lbal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbal.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(lblBalance, gbc);
        gbc.gridx = 1;
        contentPanel.add(lbal, gbc);
        
        // Withdraw Amount:
        JLabel lblAmount = new JLabel("Withdraw Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amount = new JTextField(15);
        amount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(lblAmount, gbc);
        gbc.gridx = 1;
        contentPanel.add(amount, gbc);
        
        // Button Panel:
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnCancel = new JButton("Cancel");
        
        // Style buttons: Blue for Withdraw, Red for Cancel, square edges
        btnWithdraw.setBackground(new Color(0, 102, 204)); // Blue
        btnWithdraw.setForeground(Color.WHITE);
        btnWithdraw.setFocusPainted(false);
        btnWithdraw.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        btnWithdraw.setFont(new Font("Segoe UI", Font.BOLD, 15));  // Bigger font size
        btnWithdraw.setPreferredSize(new Dimension(80, 30));      // Bigger button size
        
        btnCancel.setBackground(new Color(220, 53, 69)); // Red
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 15));    // Bigger font size
        btnCancel.setPreferredSize(new Dimension(80, 30));        // Slightly smaller cancel button
        
        btnPanel.add(btnWithdraw);
        btnPanel.add(btnCancel);
        
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPanel.add(btnPanel, gbc);
        
        // Add panel to frame
        getContentPane().add(contentPanel);
        pack();
        
        // Action listeners
        btnFind.addActionListener(this::findAccount);
        btnWithdraw.addActionListener(this::withdrawAmount);
        btnCancel.addActionListener(e -> dispose());
    }
    
    public void findAccount(ActionEvent evt) {
        String accno = txtaccno.getText();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("SELECT c.cust_id, c.firstname, c.lastname, a.balance FROM customer c, account a WHERE c.cust_id = a.cust_id AND a.acc_id = ?");
            insert.setString(1, accno);
            rs1 = insert.executeQuery();

            if (rs1.next()) {
                jLabel7.setText(rs1.getString("cust_id"));
                txtfname.setText(rs1.getString("firstname"));
                txtlame.setText(rs1.getString("lastname"));
                lbal.setText(rs1.getString("balance"));
            } else {
                JOptionPane.showMessageDialog(this, "Account not found");
                clearFields();
            }
        } catch (Exception ex) {
            Logger.getLogger(withdraw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void withdrawAmount(ActionEvent evt) {
        String accno = txtaccno.getText();
        String cust_id = jLabel7.getText();
        String date = jLabel6.getText();
        String balanceStr = lbal.getText();
        String amountStr = amount.getText();

        if (accno.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter required fields");
            return;
        }

        try {
            double currentBalance;
            double withdrawAmount;

            try {
                currentBalance = Double.parseDouble(balanceStr);
                withdrawAmount = Double.parseDouble(amountStr);

                if (withdrawAmount <= 0) {
                    JOptionPane.showMessageDialog(this, "Withdraw amount must be greater than 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
                return;
            }

            if (withdrawAmount > currentBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance");
                return;
            }

            con1.setAutoCommit(false);
            String withdrawSql = "INSERT INTO withdraw(acc_id, cust_id, date, balance, withdraw) VALUES (?, ?, ?, ?, ?)";
            insert = con1.prepareStatement(withdrawSql);
            insert.setString(1, accno);
            insert.setString(2, cust_id);
            insert.setString(3, date);
            insert.setString(4, balanceStr);
            insert.setString(5, amountStr);
            insert.executeUpdate();

            String updateSql = "UPDATE account SET balance = balance - ? WHERE acc_id = ?";
            insert2 = con1.prepareStatement(updateSql);
            insert2.setString(1, amountStr);
            insert2.setString(2, accno);
            insert2.executeUpdate();

            con1.commit();
            JOptionPane.showMessageDialog(this, "Withdrawal successful");
            clearFields();
        } catch (Exception ex) {
            try {
                if (con1 != null) con1.rollback();
            } catch (SQLException e) {
                Logger.getLogger(withdraw.class.getName()).log(Level.SEVERE, null, e);
            }
            Logger.getLogger(withdraw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearFields() {
        txtaccno.setText("");
        txtfname.setText("");
        txtlame.setText("");
        amount.setText("");
        jLabel7.setText("-");
        lbal.setText("-");
    }
    
    private void date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        jLabel6.setText(dtf.format(now));
    }
}
