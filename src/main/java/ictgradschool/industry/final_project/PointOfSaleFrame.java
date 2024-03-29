package ictgradschool.industry.final_project;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class PointOfSaleFrame extends JFrame {
    private JPanel mainPanel;
    private JList<Product> inventoryList;
    private DefaultListModel<Product> inventoryModel;
    private JList<String> cartList;
    private DefaultListModel<String> cartModel;
    private Map<Product, Integer> cartContents;
    private JButton addToCartButton, removeFromCartButton, checkoutButton, goBackButton;
    private JLabel totalCostLabel;
    private double totalCost = 0.0;
    private WelcomeFrame welcomeFrame;

    public PointOfSaleFrame(List<Product> inventory, WelcomeFrame welcomeFrame) {
        setTitle("Point of Sale");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.welcomeFrame = welcomeFrame;
        initializeComponents();
        loadInventory(inventory);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartContents = new HashMap<>();
        // set grey split line but cannot vertically alignment.
        inventoryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

                return c;
            }
        });
        addToCartButton = new JButton("Add to cart");
        removeFromCartButton = new JButton("Remove from cart");
        checkoutButton = new JButton("Checkout");
        goBackButton = new JButton("Go Back");

        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        checkoutButton.addActionListener(e -> checkout());
        goBackButton.addActionListener(e -> goBack());


        totalCostLabel = new JLabel("Total Cost: $0.00");

        // set up the components
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addToCartButton);
        buttonsPanel.add(removeFromCartButton);
        buttonsPanel.add(checkoutButton);
        buttonsPanel.add(goBackButton);

        mainPanel.add(new JScrollPane(inventoryList), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(totalCostLabel, BorderLayout.NORTH);

        // add mainPanel into Frame
        this.add(mainPanel);
        this.setVisible(true);
    }

    private void goBack() {
        this.setVisible(false);
        welcomeFrame.setVisible(true);
    }

    private void updateCartDisplay() {
        cartModel.clear();
        totalCost = 0.0;
        cartContents.forEach((product, quantity) -> {
            double cost = quantity * product.getPrice();
            totalCost += cost;
            cartModel.addElement(product.getName() + " x" + quantity + " = $" + String.format("%.2f", cost));

        });
        totalCostLabel.setText("Total Cost: $" + String.format("%.2f", totalCost));
    }
    private void loadInventory(List<Product> inventory) {
        for (Product product : inventory) {
            if (product.getStockQuantity() > 0) {
                inventoryModel.addElement(product);
            }
        }
    }

    private void addToCart() {
        // get select product
        Product selectedProduct = inventoryList.getSelectedValue();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.", "No product selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // check stock
        if (selectedProduct.getStockQuantity() <= 0) {
            JOptionPane.showMessageDialog(this, "This product is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // reduce stock quantity and renew stock list
        selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - 1);
        if (selectedProduct.getStockQuantity() == 0) {
            inventoryModel.removeElement(selectedProduct);
        } else {
            inventoryList.repaint();
        }
        CartItem cartItem = new CartItem(selectedProduct, 1);
        cartModel.addElement(cartItem.toString());
        cartContents.put(selectedProduct, cartContents.getOrDefault(selectedProduct, 0) + 1);
        updateCartDisplay();
        updateTotalCost();
    }

    private void updateTotalCost() {
        totalCost = 0.0;
        cartContents.forEach((product, quantity) -> {
            totalCost += product.getPrice() * quantity;
        });
        totalCostLabel.setText(String.format("Total: $%.2f", totalCost));
    }

    private void removeFromCart() {
        // if user has selected an entry in cartList
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove from the cart.", "No product selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = cartModel.get(selectedIndex);
        String productName = selectedItem.split(" x")[0];
        Product productToRemove = null;

        for (Map.Entry<Product, Integer> entry : cartContents.entrySet()) {
            if (entry.getKey().getName().equals(productName)) {
                productToRemove = entry.getKey();
                break;
            }
        }

        if (productToRemove != null) {
            int currentQty = cartContents.get(productToRemove);
            if (currentQty > 1) {
                cartContents.put(productToRemove, currentQty - 1);
            } else {
                cartContents.remove(productToRemove);
            }
            productToRemove.setStockQuantity(productToRemove.getStockQuantity() + 1);

            if (!inventoryModel.contains(productToRemove)) {
                inventoryModel.addElement(productToRemove);
            } else {
                inventoryList.repaint();
            }
            updateCartDisplay();
            updateTotalCost();
        }
        else {
            JOptionPane.showMessageDialog(this, "Error finding product in cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkout() {
        if (cartContents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // renew stock quantity;
        cartContents.forEach((product, quantity) -> {
        });

        // generate receipt here
        generateReceipt();

        // clear cart
        cartContents.clear();
        cartModel.clear();
        updateCartDisplay();

        JOptionPane.showMessageDialog(this, "Checkout completed.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateReceipt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Receipt");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File receiptFile = fileChooser.getSelectedFile();

            List<String> receiptLines = new ArrayList<>();
            receiptLines.add("--------------------------------");
            double totalCost = 0.0;

            for (Map.Entry<Product, Integer> entry : cartContents.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                double cost = product.getPrice() * quantity;
                totalCost += cost;

                String unitCost = quantity > 1 ? String.format("($%.2f)", product.getPrice()) : "";
                receiptLines.add(String.format("%d  %s %s    $%.2f", quantity, product.getName(), unitCost, cost));
            }

            receiptLines.add("================================");
            receiptLines.add(String.format("   TOTAL                 $%.2f", totalCost));
            receiptLines.add("--------------------------------");

            ProductFileHandler fileHandler = new ProductFileHandler();
            fileHandler.writeFile(receiptFile.getAbsolutePath(), receiptLines);

            JOptionPane.showMessageDialog(this, "Receipt has been saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Checkout canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
