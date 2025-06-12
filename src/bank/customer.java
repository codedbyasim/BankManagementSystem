package bank;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;

public class customer extends javax.swing.JInternalFrame {

    Connection con1;
    PreparedStatement insert;

    public customer() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        initComponents();
        loadBranches();
        autoId();
    }

    public class Branch {
        int id;
        String name;

        public Branch(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void initComponents() {
        setBorder(null);
        setClosable(true);
        setTitle("New Customer");

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Customer Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblCustId = new JLabel("Customer ID:");
        custIdLabel = new JLabel("CS000");
        custIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        custIdLabel.setForeground(new Color(0, 102, 204));

        JLabel lblFname = new JLabel("First Name:");
        txtfname = new JTextField(15);

        JLabel lblLname = new JLabel("Last Name:");
        txtlname = new JTextField(15);

        JLabel lblStreet = new JLabel("Street:");
        txtstreet = new JTextField(15);

        JLabel lblCity = new JLabel("City:");
        txtcity = new JTextField(15);

        JLabel lblBranch = new JLabel("Branch:");
        txtbranch = new JComboBox<>();
        txtbranch.setRenderer(new BasicComboBoxRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return c;
            }
        });

        JLabel lblMobile = new JLabel("Mobile:");
        txtmobile = new JTextField(15);

        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 153, 76));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(evt -> addCustomer());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(evt -> dispose());

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblCustId)
                .addComponent(lblFname)
                .addComponent(lblLname)
                .addComponent(lblStreet)
                .addComponent(lblCity)
                .addComponent(lblBranch)
                .addComponent(lblMobile))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(custIdLabel)
                .addComponent(txtfname)
                .addComponent(txtlname)
                .addComponent(txtstreet)
                .addComponent(txtcity)
                .addComponent(txtbranch)
                .addComponent(txtmobile)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(addButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblCustId).addComponent(custIdLabel))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblFname).addComponent(txtfname))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblLname).addComponent(txtlname))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblStreet).addComponent(txtstreet))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblCity).addComponent(txtcity))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblBranch).addComponent(txtbranch))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMobile).addComponent(txtmobile))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(addButton).addComponent(cancelButton))
        );

        contentPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(contentPanel);
        pack();
    }

    public JLabel custIdLabel;
    public JTextField txtfname;
    public JTextField txtlname;
    public JTextField txtstreet;
    public JTextField txtcity;
    public JComboBox<Branch> txtbranch;
    public JTextField txtmobile;

    private void loadBranches() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("SELECT * FROM branch");
            ResultSet rs = insert.executeQuery();
            txtbranch.removeAllItems();
            while (rs.next()) {
                txtbranch.addItem(new Branch(rs.getInt("id"), rs.getString("branch")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoId() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            Statement s = con1.createStatement();
            ResultSet rs = s.executeQuery("SELECT MAX(cust_id) FROM customer");
            if (rs.next() && rs.getString(1) != null) {
                long id = Long.parseLong(rs.getString(1).substring(2));
                id++;
                custIdLabel.setText("CS" + String.format("%03d", id));
            } else {
                custIdLabel.setText("CS001");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addCustomer() {
        try {
            String id = custIdLabel.getText();
            String fname = txtfname.getText();
            String lname = txtlname.getText();
            String street = txtstreet.getText();
            String city = txtcity.getText();
            Branch branch = (Branch) txtbranch.getSelectedItem();
            String mobile = txtmobile.getText();

            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("INSERT INTO customer(cust_id, firstname, lastname, street, city, branch, phone) VALUES (?, ?, ?, ?, ?, ?, ?)");
            insert.setString(1, id);
            insert.setString(2, fname);
            insert.setString(3, lname);
            insert.setString(4, street);
            insert.setString(5, city);
            insert.setInt(6, branch.id);
            insert.setString(7, mobile);
            insert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer successfully added!");

            autoId();
            txtfname.setText("");
            txtlname.setText("");
            txtstreet.setText("");
            txtcity.setText("");
            txtbranch.setSelectedIndex(-1);
            txtmobile.setText("");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
