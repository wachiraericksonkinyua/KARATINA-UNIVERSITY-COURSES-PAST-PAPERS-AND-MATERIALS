import javax.swing.*;
// Main class TextFieldExample
class TextFieldExample
{
public static void main(String args[])
{
// Creating a JFrame object with title "TextField Example."
JFrame f= new JFrame("TextField Example");
// Creating two JTextField objects
JTextField t1, t2;// Initializing the first JTextField with default text "Welcome to Javatpoint."
t1 = new JTextField("Welcome to Javatpoint.");
// Setting the position and size of the first JTextField
t1.setBounds(50,100, 200,30);
// Initializing the second JTextField with default text "AWT Tutorial"
t2 = new JTextField("AWT Tutorial");
// Setting the position and size of the second JTextField
t2.setBounds(50,150, 200,30);
// Adding JTextFields to the JFrame
f.add(t1);
f.add(t2);
// Setting the size of the JFrame
f.setSize(400,400);
// Setting layout to null to use absolute positioning
f.setLayout(null);
// Making the JFrame visible
f.setVisible(true);
}
}