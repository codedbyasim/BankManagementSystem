package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class login extends javax.swing.JFrame {

    Connection con1;
    PreparedStatement insert;
    ResultSet rs1;

    public login() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtuser = new javax.swing.JTextField();
        txtpass = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - NBS");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Login"));
        jPanel1.setBackground(new java.awt.Color(240, 248, 255));

        jLabel1.setText("Username");
        jLabel2.setText("Password");

        jButton1.setText("Login");
        jButton1.setBackground(new java.awt.Color(0, 102, 204));
        jButton1.setForeground(java.awt.Color.white);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginAction(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.setBackground(new java.awt.Color(255, 51, 51));
        jButton2.setForeground(java.awt.Color.white);
        jButton2.addActionListener(evt -> System.exit(0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtuser, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpass, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(20, 20, 20)
                .addComponent(jButton2)
                .addGap(90, 90, 90))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createSequentialGroup()
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtuser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtpass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(30)
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 28));
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setText("Banking System");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(30)
                .addComponent(jLabel3)
                .addGap(20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void loginAction(java.awt.event.ActionEvent evt) {
        try {
            String username = txtuser.getText();
            String password = new String(txtpass.getPassword());

            if (verifyLogin(username, password)) {
                new mainmenu().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username or password is incorrect.");
                txtuser.setText("");
                txtpass.setText("");
                txtuser.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login Failed: " + e.getMessage());
        }
    }

    // نیا method جو صرف verify کرے login credentials کو
    public boolean verifyLogin(String username, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankproject", "root", "");

        insert = con1.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
        insert.setString(1, username);
        insert.setString(2, password);
        rs1 = insert.executeQuery();

        return rs1.next();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                 javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // fallback to default
        }

        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtpass;
    private javax.swing.JTextField txtuser;
    // End of variables declaration
}
