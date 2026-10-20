import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    private static final String NAME_PLACEHOLDER = "your name here";
    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField passwordField;

    public GUI() {
        setTitle("Enter your Name");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Enter your Name", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        content.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Enter your name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(NAME_PLACEHOLDER, 18);
        nameField.setForeground(Color.GRAY);
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
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Enter your phone number:");
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(18);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Enter your password:");
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        formPanel.add(passwordField, gbc);

        content.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton submitButton = new JButton();
        submitButton.setPreferredSize(new Dimension(90, 90));
        submitButton.setIcon(loadIcon("smiley.jpg"));
        submitButton.setToolTipText("Submit");
        submitButton.addActionListener(e -> showInputValues());
        buttonPanel.add(submitButton);

        content.add(buttonPanel, BorderLayout.SOUTH);
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
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
