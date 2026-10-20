import javax.swing.*;
import java.awt.event.*;
public class RectangleAreaCalculator implements ActionListener {
JTextField lengthField, widthField, resultField;
JButton calculateButton;
RectangleAreaCalculator() {
// Create JFrame
JFrame frame = new JFrame("Rectangle Area Calculator");
frame.setSize(300, 200);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setLayout(null);
// Create JLabels
JLabel lengthLabel = new JLabel("Length:");
lengthLabel.setBounds(20, 20, 60, 20);
JLabel widthLabel = new JLabel("Width:");
widthLabel.setBounds(20, 50, 60, 20);
JLabel resultLabel = new JLabel("Area:");
resultLabel.setBounds(20, 80, 60, 20);
// Create JTextFields
lengthField = new JTextField();
lengthField.setBounds(90, 20, 150, 20);
widthField = new JTextField();
widthField.setBounds(90, 50, 150, 20);
resultField = new JTextField();
resultField.setBounds(90, 80, 150, 20);
resultField.setEditable(false);
// Create JButton
calculateButton = new JButton("Calculate");
calculateButton.setBounds(90, 110, 100, 30);
calculateButton.addActionListener(this);
// Add components to the frame
frame.add(lengthLabel);
frame.add(widthLabel);
frame.add(resultLabel);
frame.add(lengthField);frame.add(widthField);
frame.add(resultField);
frame.add(calculateButton);
// Make the frame visible
frame.setVisible(true);
}
public void actionPerformed(ActionEvent e) {
// Calculate area when the button is clicked
if (e.getSource() == calculateButton) {
try {
// Get length and width from text fields
double length = Double.parseDouble(lengthField.getText());
double width = Double.parseDouble(widthField.getText());
// Calculate area
double area = length * width;
// Display result in resultField
resultField.setText(String.format("%.2f", area));
} catch (NumberFormatException ex) {
// Handle invalid input
JOptionPane.showMessageDialog(null, "Please enter valid numbers for length andwidth.");
}
}
}
public static void main(String[] args) {
// Create an instance of RectangleAreaCalculator
new RectangleAreaCalculator();
}
}