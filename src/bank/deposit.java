package bank;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class deposit extends javax.swing.JInternalFrame {

    Connection con1;
    PreparedStatement insert, insert2;
    ResultSet rs1;

    private JTextField txtAccNo, txtFName, txtLName, txtAmount;
    private JLabel lblBalance, lblCustId, lblDate;
    private JButton btnFind, btnDeposit, btnCancel;

    public deposit() {
        initComponents();
        setFrameStyle();
        showCurrentDate();
    }

    private void setFrameStyle() {
        setTitle("💰 Deposit Funds");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        putClientProperty("JInternalFrame.titlePaneBackground", new Color(40, 120, 200));
        putClientProperty("JInternalFrame.titlePaneForeground", Color.WHITE);
    }

    private void showCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        lblDate.setText(dtf.format(LocalDateTime.now()));
    }

    private void initComponents() {
        // TITLE
        JLabel lblTitle = new JLabel("Deposit Money", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 120, 200));

        // Account Lookup Panel
        JPanel panelSearch = new JPanel(new BorderLayout(10, 10));
        panelSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Find Account", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 14)));

        txtAccNo = createInputField();
        btnFind = createButton("Search");
        btnFind.addActionListener(this::onFindAccount);

        JPanel searchInput = new JPanel(new BorderLayout(5, 5));
        searchInput.add(new JLabel("Account No:"), BorderLayout.WEST);
        searchInput.add(txtAccNo, BorderLayout.CENTER);
        searchInput.add(btnFind, BorderLayout.EAST);

        panelSearch.add(searchInput, BorderLayout.NORTH);

        // Customer Info Panel
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtFName = createInputField(false);
        txtLName = createInputField(false);
        lblCustId = new JLabel("N/A");

        panelInfo.add(new JLabel("Customer ID:"));
        panelInfo.add(lblCustId);
        panelInfo.add(new JLabel("First Name:"));
        panelInfo.add(txtFName);
        panelInfo.add(new JLabel("Last Name:"));
        panelInfo.add(txtLName);

        // Balance and Deposit Panel
        JPanel panelTransact = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTransact.setBorder(new EmptyBorder(10, 10, 10, 10));
        lblBalance = new JLabel("0.00");
        txtAmount = createInputField();

        lblDate = new JLabel();

        panelTransact.add(new JLabel("Current Balance:"));
        panelTransact.add(lblBalance);
        panelTransact.add(new JLabel("Deposit Amount:"));
        panelTransact.add(txtAmount);
        panelTransact.add(new JLabel("Date:"));
        panelTransact.add(lblDate);

        // Buttons
        btnDeposit = createButton("Deposit");
        btnCancel = createButton("Cancel");
        btnDeposit.addActionListener(this::onDeposit);
        btnCancel.addActionListener(e -> dispose());

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(btnDeposit);
        panelButtons.add(btnCancel);

        // Full Layout
        setLayout(new BorderLayout(15, 15));
        add(lblTitle, BorderLayout.NORTH);
        add(panelSearch, BorderLayout.WEST);
        add(panelInfo, BorderLayout.CENTER);
        add(panelTransact, BorderLayout.EAST);
        add(panelButtons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800, 300));
        pack();
    }

    private JTextField createInputField() {
        return createInputField(true);
    }

    private JTextField createInputField(boolean editable) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        field.setEditable(editable);
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(40, 120, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private void onFindAccount(ActionEvent evt) {
        String accNo = txtAccNo.getText();
        try {
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("SELECT c.cust_id, c.firstname, c.lastname, a.balance FROM customer c, account a WHERE c.cust_id = a.cust_id AND a.acc_id = ?");
            insert.setString(1, accNo);
            rs1 = insert.executeQuery();

            if (!rs1.next()) {
                JOptionPane.showMessageDialog(this, "Account not found.");
                txtFName.setText(""); txtLName.setText(""); lblBalance.setText("0.00"); lblCustId.setText("N/A");
            } else {
                lblCustId.setText(rs1.getString("cust_id"));
                txtFName.setText(rs1.getString("firstname"));
                txtLName.setText(rs1.getString("lastname"));
                lblBalance.setText(rs1.getString("balance"));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onDeposit(ActionEvent evt) {
        String accNo = txtAccNo.getText();
        String custId = lblCustId.getText();
        String date = lblDate.getText();
        String currentBal = lblBalance.getText();
        String depositAmt = txtAmount.getText();

        if (accNo.isEmpty() || custId.equals("N/A") || depositAmt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(depositAmt);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Deposit amount must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
            return;
        }

        try {
            con1.setAutoCommit(false);

            String insertSQL = "INSERT INTO deposit(acc_id, cust_id, date, balance, deposit) VALUES (?, ?, ?, ?, ?)";
            insert = con1.prepareStatement(insertSQL);
            insert.setString(1, accNo);
            insert.setString(2, custId);
            insert.setString(3, date);
            insert.setString(4, currentBal);
            insert.setString(5, depositAmt);
            insert.executeUpdate();

            String updateSQL = "UPDATE account SET balance = balance + ? WHERE acc_id = ?";
            insert2 = con1.prepareStatement(updateSQL);
            insert2.setString(1, depositAmt);
            insert2.setString(2, accNo);
            insert2.executeUpdate();

            con1.commit();
            JOptionPane.showMessageDialog(this, "Deposit successful!");
            txtAmount.setText("");
            onFindAccount(null); // Refresh

        } catch (Exception ex) {
            try { con1.rollback(); } catch (SQLException e) {}
            JOptionPane.showMessageDialog(this, "Transaction failed: " + ex.getMessage());
        }
    }
}
