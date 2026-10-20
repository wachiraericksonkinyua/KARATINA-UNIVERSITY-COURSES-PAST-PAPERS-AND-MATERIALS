import javax.swing.*;

public class ButtonExample3 {
    ButtonExample3(){
    JFrame f=new JFrame("button example");
    JButton b=new JButton(new ImageIcon(""));
    b.setBounds(100,100,100,40);
    f.add(b);
    f.setLayout(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        new ButtonExample3();
    }

}

