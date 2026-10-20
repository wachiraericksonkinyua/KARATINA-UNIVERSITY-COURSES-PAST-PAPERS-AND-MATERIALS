import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SmileyForm extends JFrame {
    private static final String NAME_PLACEHOLDER = "your name here";
    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField passwordField;

    public SmileyForm() {
        setTitle("Enter your Name");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Enter your Name");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        content.add(titleLabel);

        JPanel grayPanel = new JPanel();
        grayPanel.setLayout(new BoxLayout(grayPanel, BoxLayout.Y_AXIS));
        grayPanel.setBackground(new Color(230, 230, 230));
        grayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        grayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(NAME_PLACEHOLDER);
        nameField.setMaximumSize(new Dimension(280, 30));
        nameField.setForeground(Color.GRAY);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (NAME_PLACEHOLDER.equals(nameField.getText())) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isBlank()) {
                    nameField.setText(NAME_PLACEHOLDER);
                    nameField.setForeground(Color.GRAY);
                }
            }
        });
        grayPanel.add(nameField);
        grayPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel phoneRow = new JPanel(new BorderLayout(8, 0));
        phoneRow.setOpaque(false);
        JLabel phoneLabel = new JLabel("Enter your phone number");
        phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        phoneRow.add(phoneLabel, BorderLayout.WEST);
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(130, 28));
        phoneField.setMaximumSize(new Dimension(130, 28));
        phoneRow.add(phoneField, BorderLayout.EAST);
        grayPanel.add(phoneRow);
        grayPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel passwordRow = new JPanel(new BorderLayout(8, 0));
        passwordRow.setOpaque(false);
        JLabel passwordLabel = new JLabel("Enter your password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        passwordRow.add(passwordLabel, BorderLayout.WEST);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(130, 28));
        passwordField.setMaximumSize(new Dimension(130, 28));
        passwordRow.add(passwordField, BorderLayout.EAST);
        grayPanel.add(passwordRow);

        content.add(grayPanel);
        content.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton smileyButton = new JButton(loadIcon("smiley.jpg"));
        smileyButton.setPreferredSize(new Dimension(72, 72));
        smileyButton.setBorder(BorderFactory.createEmptyBorder());
        smileyButton.setContentAreaFilled(false);
        smileyButton.addActionListener(e -> showInputValues());
        iconPanel.add(smileyButton);

        content.add(iconPanel);
        setContentPane(content);
    }

    private void showInputValues() {
        String name = nameField.getText();
        if (NAME_PLACEHOLDER.equals(name)) {
            name = "";
        }
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        String message = "Name: " + name + "\n"
                + "Phone: " + phone + "\n"
                + "Password: " + password;

        JOptionPane.showMessageDialog(this, message, "Entered Values", JOptionPane.INFORMATION_MESSAGE);
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path.startsWith("/") ? path : "/" + path);
        if (resource != null) {
            return new ImageIcon(resource);
        }
        return new ImageIcon(path);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmileyForm().setVisible(true));
    }
}
