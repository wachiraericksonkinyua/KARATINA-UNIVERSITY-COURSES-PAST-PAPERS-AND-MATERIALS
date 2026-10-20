import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ImagePOS extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private double total = 0.0;

    public ImagePOS() {
        setTitle("Rome - Advanced POS");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. PRODUCT GRID (Left Side) ---
        // Adjust the grid (rows, cols) based on your 10 items
        JPanel productPanel = new JPanel(new GridLayout(0, 2, 10, 10)); 
        productPanel.setBorder(BorderFactory.createTitledBorder("Product Menu"));
        
        // Add your 10 items here. 
        // Ensure the filenames match exactly what is in your 'products' folder!
        addProductItem("Burger", 550.00, "products/burger.jpeg", productPanel);
        addProductItem("Pizza", 1200.00, "products/pizza.jpeg", productPanel);
        addProductItem("Soda", 100.00, "products/soda.jpeg", productPanel);
        addProductItem("cake", 120.00,"products/cake.jpeg", productPanel);

        JScrollPane menuScroll = new JScrollPane(productPanel);
        menuScroll.setPreferredSize(new Dimension(450, 0));
        add(menuScroll, BorderLayout.WEST);

        // --- 2. ORDER TABLE (Center) ---
        String[] columns = {"Item Name", "Price (Ksh)"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 3. CONTROLS (Bottom) ---
        JPanel footer = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: Ksh 0.00  ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel btnPanel = new JPanel();
        JButton btnRemove = new JButton("Remove Selected");
        JButton btnCheckout = new JButton("Checkout");
        JButton btnClear = new JButton("Clear All");

        // Logic to Remove Selected Item
        btnRemove.addActionListener(e -> removeSelectedItem());
        
        // Logic for Checkout
        btnCheckout.addActionListener(e -> {
            if(model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
            } else {
                JOptionPane.showMessageDialog(this, "Proceeding to Payment...\nTotal: Ksh " + total);
                resetCart();
            }
        });

        btnClear.addActionListener(e -> resetCart());

        btnPanel.add(btnRemove);
        btnPanel.add(btnClear);
        btnPanel.add(btnCheckout);

        footer.add(lblTotal, BorderLayout.WEST);
        footer.add(btnPanel, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private void addProductItem(String name, double price, String imgPath, JPanel panel) {
        JButton btn = new JButton(name + " (Ksh " + price + ")");
        
        // Try to load the image
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            // Resize image to fit the button (e.g., 80x80)
            Image scaledImg = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.out.println("Image not found: " + imgPath);
        }

        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        
        btn.addActionListener(e -> {
            model.addRow(new Object[]{name, price});
            updateTotal(price);
        });
        panel.add(btn);
    }

    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            double price = (double) model.getValueAt(selectedRow, 1);
            updateTotal(-price); // Subtract price
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item in the table to remove.");
        }
    }

    private void updateTotal(double amount) {
        total += amount;
        lblTotal.setText(String.format("Total: Ksh %.2f", total));
    }

    private void resetCart() {
        model.setRowCount(0);
        total = 0.0;
        lblTotal.setText("Total: Ksh 0.00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImagePOS().setVisible(true));
    }
}