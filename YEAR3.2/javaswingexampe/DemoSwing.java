//EXAMPLE OF SWING BY INHERITANCE
import javax.swing.*;
public class DemoSwing extends JFrame{//inheriting JFrame
JFrame f;
DemoSwing(){
JButton b=new JButton("click");//create button
b.setBounds(130,100,100, 40);
add(b);//adding button on frame
setSize(400,500);
setLayout(null);
//setLayout(new FlowLayout());  // Components flow left to right
setVisible(true);
}
public static void main(String[] args) {
new DemoSwing();
}}