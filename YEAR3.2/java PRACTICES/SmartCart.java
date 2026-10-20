import java.util.*;
import java.io.*;

class Item {
    String name;
    double price;
    int quantity;

    Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    double getTotal() {
        return price * quantity;
    }
}

public class SmartCart {

    static ArrayList<Item> cart = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    // 🔐 LOGIN SYSTEM
    static boolean login() {
        System.out.println("=== LOGIN ===");
        System.out.print("Username: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String pass = input.nextLine();

        return user.equals("admin") && pass.equals("1234");
    }

    public static void main(String[] args) {

        if (!login()) {
            System.out.println("Access Denied!");
            return;
        }

        System.out.println("Login successful!\n");

        while (true) {
            System.out.println("\n=== SMART CART MENU ===");
            System.out.println("1. Add Item");
            System.out.println("2. View Cart");
            System.out.println("3. Remove Item");
            System.out.println("4. Update Quantity");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1: addItem(); break;
                case 2: viewCart(); break;
                case 3: removeItem(); break;
                case 4: updateItem(); break;
                case 5: checkout(); break;
                case 6: System.exit(0);
                default: System.out.println("Invalid!");
            }
        }
    }

    static void addItem() {
        System.out.print("Item name: ");
        String name = input.nextLine();

        System.out.print("Price: ");
        double price = input.nextDouble();

        System.out.print("Quantity: ");
        int qty = input.nextInt();
        input.nextLine();

        cart.add(new Item(name, price, qty));
        System.out.println("Item added!");
    }

    static void viewCart() {
        double total = 0;

        System.out.println("\n--- CART ---");
        for (Item item : cart) {
            System.out.println(item.name + " x" + item.quantity + " = " + item.getTotal());
            total += item.getTotal();
        }

        System.out.println("Total: " + total);
    }

    static void removeItem() {
        System.out.print("Enter item to remove: ");
        String name = input.nextLine();

        cart.removeIf(item -> item.name.equalsIgnoreCase(name));
        System.out.println("Removed!");
    }

    static void updateItem() {
        System.out.print("Enter item to update: ");
        String name = input.nextLine();

        for (Item item : cart) {
            if (item.name.equalsIgnoreCase(name)) {
                System.out.print("New quantity: ");
                item.quantity = input.nextInt();
                input.nextLine();
                System.out.println("Updated!");
                return;
            }
        }

        System.out.println("Item not found!");
    }

    static void checkout() {
        double total = 0;

        for (Item item : cart) {
            total += item.getTotal();
        }

        // 💸 DISCOUNT
        if (total > 1000) {
            total *= 0.9;
            System.out.println("10% discount applied!");
        }

        // 💳 PAYMENT
        System.out.println("\nPayment Method:");
        System.out.println("1. Cash\n2. M-Pesa");
        int pay = input.nextInt();
        input.nextLine();

        if (pay == 2) {
            System.out.print("Enter phone number: ");
            String phone = input.nextLine();
            System.out.println("Processing M-Pesa payment for " + phone + "...");
        }

        System.out.println("Payment successful!");

        saveReceipt(total);
        cart.clear();
    }
    static void saveReceipt(double total) {
        try {
            FileWriter writer = new FileWriter("receipt.txt");

            writer.write("=== RECEIPT ===\n");
            for (Item item : cart) {
                writer.write(item.name + " x" + item.quantity + " = " + item.getTotal() + "\n");
            }
            writer.write("TOTAL: " + total);

            writer.close();
            System.out.println("Receipt saved to file!");

        } catch (IOException e) {
            System.out.println("Error saving receipt!");
        }
    }
}