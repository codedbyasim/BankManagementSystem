package bank;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainmenu extends JFrame {

    private JDesktopPane desktopPane;

    public mainmenu() {
        initComponents();
    }

    private void initComponents() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        setTitle("Banking System - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ======= TOP HEADER =======
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(250, 250, 250));
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel dashboardLabel = new JLabel("Banking System");
        dashboardLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        dashboardLabel.setForeground(new Color(45, 85, 155));
        topPanel.add(dashboardLabel, BorderLayout.WEST);

        // ======= MAIN CENTER PANEL =======
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 248, 255),
                        getWidth(), getHeight(), new Color(220, 235, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // ======= DESKTOP PANE =======
        desktopPane = new JDesktopPane();
        desktopPane.setOpaque(false);

        // ======= BUTTON PANEL =======
        JPanel buttonGrid = new JPanel(new GridLayout(2, 4, 30, 30)); // increased gaps
        buttonGrid.setOpaque(false);
        buttonGrid.setPreferredSize(new Dimension(800, 300));

        String[][] buttonsWithIcons = {
                {"Customer", "src/resources/customer.png"},
                {"Account", "src/resources/account.png"},
                {"Deposit", "src/resources/deposit.png"},
                {"Withdraw", "src/resources/withdraw.png"},
                {"Transfer", "src/resources/transfer.png"},
                {"Check Balance", "src/resources/balance.png"},
                {"Customer Report", "src/resources/report.png"},
                {"Create User", "src/resources/user.png"}
        };

        for (String[] item : buttonsWithIcons) {
            String label = item[0];
            String iconPath = item[1];

            JButton btn = createTileButton(label, iconPath);
            btn.addActionListener(e -> openFrameByLabel(label));
            buttonGrid.add(btn);
        }

        // ======= CENTER PANEL WITH SEPARATOR =======
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanelWrapper = new JPanel(new GridBagLayout());
        buttonPanelWrapper.setOpaque(false);
        buttonPanelWrapper.add(buttonGrid);
        buttonPanelWrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50)); // spacing around buttons

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(180, 180, 180)); // gray color line

        centerPanel.add(buttonPanelWrapper);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(separator);
        centerPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(centerPanel, BorderLayout.NORTH);
        mainPanel.add(desktopPane, BorderLayout.CENTER);

        // ======= ADD PANELS TO FRAME =======
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createTileButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(45, 85, 155));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setPreferredSize(new Dimension(150, 120));
        btn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // increased padding

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 240)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(230, 240, 255));
                btn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 240)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240)));
            }
        });

        return btn;
    }

    private void openFrameByLabel(String label) {
        JInternalFrame frame = switch (label) {
            case "Customer" -> new customer();
            case "Account" -> new account();
            case "Deposit" -> new deposit();
            case "Withdraw" -> new withdraw();
            case "Transfer" -> new transfer();
            case "Check Balance" -> new balance();
            case "Customer Report" -> new cusreport();
            case "Create User" -> new user1();
            default -> null;
        };

        if (frame != null) {
            desktopPane.add(frame);
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(mainmenu::new);
    }
}
