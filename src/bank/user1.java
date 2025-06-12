package bank;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class user1 extends javax.swing.JInternalFrame {

    Connection con;
    PreparedStatement pst;

    public user1() {
        initComponents();
        this.setClosable(true);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        tableUpdate();
    }

    private void tableUpdate() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            pst = con.prepareStatement("SELECT * FROM user");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("username"));
                model.addRow(v);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    public void addUser() {
        try {
            String name = txtName.getText().trim();
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            pst = con.prepareStatement("INSERT INTO user(name, username, password) VALUES (?, ?, ?)");
            pst.setString(1, name);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User added successfully.");
            resetForm();
            tableUpdate();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public void deleteUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
            return;
        }

        int id = Integer.parseInt(userTable.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
                pst = con.prepareStatement("DELETE FROM user WHERE id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "User deleted.");
                resetForm();
                tableUpdate();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    public void updateUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update.");
            return;
        }

        int id = Integer.parseInt(userTable.getValueAt(row, 0).toString());
        String name = txtName.getText().trim();
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        if (name.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Username cannot be empty.");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");

            if (password.isEmpty()) {
                pst = con.prepareStatement("UPDATE user SET name = ?, username = ? WHERE id = ?");
                pst.setString(1, name);
                pst.setString(2, username);
                pst.setInt(3, id);
            } else {
                pst = con.prepareStatement("UPDATE user SET name = ?, username = ?, password = ? WHERE id = ?");
                pst.setString(1, name);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setInt(4, id);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User updated successfully.");
            resetForm();
            tableUpdate();
            addButton.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtName.setText("");
        txtUser.setText("");
        txtPass.setText("");
        txtName.requestFocus();
        addButton.setEnabled(true);
        showPasswordCheckBox.setSelected(false);
        txtPass.setEchoChar('•');
    }

    private void onTableClick(MouseEvent evt) {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            txtName.setText(userTable.getValueAt(row, 1).toString());
            txtUser.setText(userTable.getValueAt(row, 2).toString());

            int id = Integer.parseInt(userTable.getValueAt(row, 0).toString());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
                pst = con.prepareStatement("SELECT password FROM user WHERE id = ?");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String password = rs.getString("password");
                    txtPass.setText(password);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching password: " + e.getMessage());
                txtPass.setText("");
            }

            addButton.setEnabled(false);
            showPasswordCheckBox.setSelected(false);
            txtPass.setEchoChar('•');
        }
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel nameLabel = new JLabel("Name:");
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        txtName = new JTextField();
        txtUser = new JTextField();
        txtPass = new JPasswordField();

        addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");
        JButton clearButton = new JButton("Clear");

        showPasswordCheckBox = new JCheckBox("Show Password");

        // Set button fonts and sizes
        Font btnFont = new Font("Segoe UI", Font.BOLD, 15);
        Dimension btnSize = new Dimension(90, 35);

        addButton.setFont(btnFont);
        addButton.setPreferredSize(btnSize);
        updateButton.setFont(btnFont);
        updateButton.setPreferredSize(btnSize);
        deleteButton.setFont(btnFont);
        deleteButton.setPreferredSize(btnSize);
        cancelButton.setFont(btnFont);
        cancelButton.setPreferredSize(btnSize);
        clearButton.setFont(btnFont);
        clearButton.setPreferredSize(btnSize);

        // Button colors
        addButton.setBackground(new Color(30, 144, 255));     // Blue
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(30, 144, 255));  // Blue
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(30, 144, 255));  // Blue
        deleteButton.setForeground(Color.WHITE);

        cancelButton.setBackground(new Color(220, 20, 60));   // Red
        cancelButton.setForeground(Color.WHITE);

        clearButton.setBackground(new Color(30, 144, 255));   // Blue
        clearButton.setForeground(Color.WHITE);

        addButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        clearButton.setFocusPainted(false);

        // Action listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        cancelButton.addActionListener(e -> this.dispose());
        clearButton.addActionListener(e -> resetForm());

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('•');
            }
        });

        userTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Name", "Username"}));
        userTable.setRowHeight(25);
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                onTableClick(evt);
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel formPanel = new JPanel();
        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
            .addComponent(titleLabel)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(userLabel)
                    .addComponent(passLabel)
                    .addComponent(showPasswordCheckBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, 250, 250, 250)
                    .addComponent(txtUser, 250, 250, 250)
                    .addComponent(txtPass, 250, 250, 250)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(addButton)
                .addComponent(updateButton)
                .addComponent(deleteButton)
                .addComponent(clearButton)
                .addComponent(cancelButton))
            .addComponent(scrollPane)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(titleLabel)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel)
                .addComponent(txtName))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(userLabel)
                .addComponent(txtUser))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(passLabel)
                .addComponent(txtPass))
            .addComponent(showPasswordCheckBox)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(addButton)
                .addComponent(updateButton)
                .addComponent(deleteButton)
                .addComponent(clearButton)
                .addComponent(cancelButton))
            .addComponent(scrollPane)
        );

        this.add(formPanel);
        this.pack();
    }

    // Variables declaration
    private JButton addButton, deleteButton, cancelButton;
    private JTable userTable;
    private JTextField txtName, txtUser;
    private JPasswordField txtPass;
    private JCheckBox showPasswordCheckBox;

}
