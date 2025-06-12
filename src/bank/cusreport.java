package bank;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class cusreport extends javax.swing.JInternalFrame {

    Connection con1;
    PreparedStatement insert;

    public cusreport() {
        initComponents();
        table_update();
    }

    private void initComponents() {
        setTitle("Customer Report");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Customer Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));

        jTable1 = new JTable();
        jTable1.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Customer ID", "First Name", "Last Name", "Street", "City", "Branch", "Phone"
            }
        ));
        jTable1.setRowHeight(28);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(jTable1);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JButton btnCancel = new JButton("Close");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.add(btnCancel);

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 450);
    }

    private void table_update() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");
            insert = con1.prepareStatement("SELECT c.id, c.firstname, c.lastname, c.street, c.city, b.branch, c.phone FROM customer c JOIN branch b ON c.branch = b.id");
            ResultSet rs = insert.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("id"));
                row.add(rs.getString("firstname"));
                row.add(rs.getString("lastname"));
                row.add(rs.getString("street"));
                row.add(rs.getString("city"));
                row.add(rs.getString("branch"));
                row.add(rs.getString("phone"));
                df.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customer data:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration
    private JTable jTable1;
}
