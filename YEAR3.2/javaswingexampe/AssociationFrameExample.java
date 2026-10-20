import javax.swing.*;

public class AssociationFrameExample {
    
    public static void main(String[] args) {
        // Creating JFrame object (association)
        JFrame frame = new JFrame("My Association Frame");
        
        // Creating a button
        JButton button = new JButton("Click Me!");
        
        // Setting button position and size
        button.setBounds(150, 200, 120, 40);
        
        // Adding button to the frame
        frame.add(button);
        
        // Setting frame size
        frame.setSize(500, 400);
        
        // Setting layout to null for manual positioning
        frame.setLayout(null);
        
        // Making frame visible
        frame.setVisible(true);
        
        // Setting close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}