import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginForm {
public static void main(String[] args) {
// Create the main frame
JFrame frame = new JFrame("Login Form");
// Create a panel to hold components
JPanel panel = new JPanel();
// Create text fields for username and password
JTextField usernameField = new JTextField(20);
JPasswordField passwordField = new JPasswordField(20);
// Create a button for login
JButton loginButton = new JButton("Login");
// Add components to the panel
panel.add(new JLabel("Username: "));
panel.add(usernameField);
panel.add(new JLabel("Password: "));
panel.add(passwordField);
panel.add(loginButton);// Add action listener to the login button
loginButton.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
// Retrieve username and password entered by the user
String username = usernameField.getText();
String password = new String(passwordField.getPassword());
// Validate username and password
if (username.equals("admin") && password.equals("password")) {
// Show a success message if login is successful
JOptionPane.showMessageDialog(frame, "Login successful!");
} else {
// Show an error message if login fails
JOptionPane.showMessageDialog(frame, "Invalid username or password");
}
}
});
// Add the panel to the frame
frame.add(panel);
// Set frame properties
frame.setSize(300, 150);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setVisible(true);
}
}