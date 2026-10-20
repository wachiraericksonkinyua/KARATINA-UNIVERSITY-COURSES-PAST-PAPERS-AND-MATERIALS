import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimplePOS extends JFrame {
    
    // Components we need to access globally within the class
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private double total = 0.0;

    public SimplePOS() {
        // 1. Basic Frame Setup
        setTitle("Java Swing POS System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2. North Panel: Title
        JLabel title = new JLabel("QuickPOS Terminal", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // 3. West Panel: Item Buttons
        JPanel itemPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Items"));
        
        addItemButton("Burger", 5.99, itemPanel);
        addItemButton("Pizza", 8.50, itemPanel);
        addItemButton("Soda", 1.50, itemPanel);
        addItemButton("Coffee", 2.00, itemPanel);
        
        add(itemPanel, BorderLayout.WEST);

        // 4. Center Panel: The Cart (Table)
        String[] columns = {"Item Name", "Price"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 5. South Panel: Total and Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton btnClear = new JButton("Clear Cart");
        btnClear.addActionListener(e -> resetCart());

        footer.add(lblTotal);
        footer.add(btnClear);
        add(footer, BorderLayout.SOUTH);
    }

    // Helper method to create buttons and their click logic
    private void addItemButton(String name, double price, JPanel panel) {
        JButton btn = new JButton(name + " ($" + price + ")");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{name, price});
                total += price;
                lblTotal.setText(String.format("Total: $%.2f", total));
            }
        });
        panel.add(btn);
    }

    private void resetCart() {
        model.setRowCount(0);
        total = 0.0;
        lblTotal.setText("Total: $0.00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimplePOS().setVisible(true));
    }
}