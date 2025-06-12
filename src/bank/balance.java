package bank;

import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import com.formdev.flatlaf.FlatClientProperties;

public class balance extends javax.swing.JInternalFrame {

    Connection con1;
    PreparedStatement insert;
    ResultSet rs1;

    private JLabel lblCustomerID, lblFirstName, lblLastName, lblBalance;

    public balance() {
        initComponents();
        setFrameStyle();
    }

    private void setFrameStyle() {
        setTitle("Account Balance Inquiry");
        setClosable(true);
        setIconifiable(true);
        setResizable(false);

        // Use FlatLaf client properties for title bar colors (like deposit.java)
        putClientProperty("JInternalFrame.titlePaneBackground", new Color(40, 120, 200));
        putClientProperty("JInternalFrame.titlePaneForeground", Color.WHITE);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(700, 450));
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel header = new JLabel("Check Account Balance", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.putClientProperty(FlatClientProperties.STYLE, "font:$h2.font");
        mainPanel.add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel accNoLabel = new JLabel("Account No:");
        accNoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtaccno = new JTextField(20);

        JButton findBtn = new JButton("Find");
        findBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        findBtn.setBackground(new Color(40, 120, 200));  // Blue background
        findBtn.setForeground(Color.WHITE);
        findBtn.setFocusPainted(false);
        findBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        findBtn.addActionListener((ActionEvent evt) -> {
            String accno = txtaccno.getText();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
                insert = con1.prepareStatement("SELECT c.cust_id, c.firstname, c.lastname, a.balance FROM customer c, account a WHERE c.cust_id = a.cust_id AND a.acc_id = ?");
                insert.setString(1, accno);
                rs1 = insert.executeQuery();

                if (rs1.next()) {
                    lblCustomerID.setText(rs1.getString(1));
                    lblFirstName.setText(rs1.getString(2));
                    lblLastName.setText(rs1.getString(3));
                    lblBalance.setText("Rs. " + rs1.getString(4));
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Account No", "Error", JOptionPane.ERROR_MESSAGE);
                    clearLabels();
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(accNoLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(txtaccno, gbc);
        gbc.gridx = 2;
        formPanel.add(findBtn, gbc);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        JLabel lbl1 = new JLabel("Customer ID:");
        JLabel lbl2 = new JLabel("First Name:");
        JLabel lbl3 = new JLabel("Last Name:");
        JLabel lbl4 = new JLabel("Balance:");

        lblCustomerID = new JLabel("-");
        lblFirstName = new JLabel("-");
        lblLastName = new JLabel("-");
        lblBalance = new JLabel("-");

        lblCustomerID.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFirstName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLastName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBalance.setForeground(new Color(0, 102, 204));

        infoPanel.add(lbl1);
        infoPanel.add(lblCustomerID);
        infoPanel.add(lbl2);
        infoPanel.add(lblFirstName);
        infoPanel.add(lbl3);
        infoPanel.add(lblLastName);
        infoPanel.add(lbl4);
        infoPanel.add(lblBalance);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelBtn = new JButton("Close");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(220, 53, 69));  // Bootstrap-like Red
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    private void clearLabels() {
        lblCustomerID.setText("-");
        lblFirstName.setText("-");
        lblLastName.setText("-");
        lblBalance.setText("-");
    }

    // Optional method for finding account info (unchanged)
    public String[] findAccountInfo(String accno) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
        insert = con1.prepareStatement(
            "SELECT c.cust_id, c.firstname, c.lastname, a.balance FROM customer c, account a WHERE c.cust_id = a.cust_id AND a.acc_id = ?"
        );
        insert.setString(1, accno);
        rs1 = insert.executeQuery();

        if (rs1.next()) {
            return new String[] {
                rs1.getString(1),
                rs1.getString(2),
                rs1.getString(3),
                rs1.getString(4)
            };
        } else {
            return null;
        }
    }
}
