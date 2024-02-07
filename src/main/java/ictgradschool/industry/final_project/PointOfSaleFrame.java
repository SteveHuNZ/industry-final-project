package ictgradschool.industry.final_project;

import javax.swing.*;
import java.awt.*;
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
    private JButton addToCartButton, removeFromCartButton, checkoutButton;
    private JLabel totalCostLabel;
    private double totalCost = 0.0;
    public PointOfSaleFrame(List<Product> inventory){
        setTitle("Point of Sale");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeComponents();
        loadInventory(inventory);
    }
    private void initializeComponents(){
        mainPanel = new JPanel(new BorderLayout());
        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartContents = new HashMap<>();

        addToCartButton = new JButton("Add to cart");
        removeFromCartButton = new JButton("Remove from cart");
        checkoutButton = new JButton("Checkout");

        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        checkoutButton.addActionListener(e -> checkout());


        totalCostLabel = new JLabel("Total Cost: $0.00");

        // set up the components
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addToCartButton);
        buttonsPanel.add(removeFromCartButton);
        buttonsPanel.add(checkoutButton);

        mainPanel.add(new JScrollPane(inventoryList), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(totalCostLabel, BorderLayout.NORTH);

        // add mainPanel into Frame
        this.add(mainPanel);
        this.setVisible(true);
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
    private Product findProductByName(String name) {
        for (int i = 0; i < inventoryModel.getSize(); i++) {
            Product product = inventoryModel.getElementAt(i);
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private void loadInventory(List<Product> inventory){
        for(Product product : inventory){
            if(product.getStockQuantity() > 0){
                inventoryModel.addElement(product);
            }
        }
    }
    private void addToCart(){
        // get select product
        Product selectedProduct = inventoryList.getSelectedValue();
        if(selectedProduct == null){
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.", "No product selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // check stock
        if(selectedProduct.getStockQuantity() <= 0){
            JOptionPane.showMessageDialog(this, "This product is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // reduce stock quantity and renew stock list
        selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - 1);
        if(selectedProduct.getStockQuantity() == 0){
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
    private void removeFromCart(){
        // if user has selected an entry in cartList
        int selectedIndex = cartList.getSelectedIndex();
        if(selectedIndex == -1){
            JOptionPane.showMessageDialog(this,"Please select a product to remove from the cart.", "No product selected", JOptionPane.WARNING_MESSAGE);
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
            updateCartDisplay();
            updateTotalCost();
        } else {
            JOptionPane.showMessageDialog(this, "Error finding product in cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void checkout(){
        if(cartContents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // renew stock quantity;
        cartContents.forEach((product, quantity) ->{
            // TODO: if stock will renew automatically when something is added to cart. No operation needed here.
        });

        // generate receipt here
        generateReceipt();

        // clear cart
        cartContents.clear();
        cartModel.clear();
        updateCartDisplay();

        JOptionPane.showMessageDialog(this, "Checkout completed.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
    }
    private void generateReceipt(){
        //TODO: may use file chooser to store the receipt;
    }


}
