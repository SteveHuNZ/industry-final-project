package ictgradschool.industry.final_project;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InventoryManagerFrame extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String filePath;
    private JTextField identifierField, nameField, descriptionField, priceField, stockQuantityField;
    private JButton addButton, removeButton, goBackButton;
    private WelcomeFrame welcomeFrame;
    public InventoryManagerFrame() {
        setTitle("Inventory Manager");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
        initializeTable();
    }
    public InventoryManagerFrame(String filePath, WelcomeFrame welcomeFrame){
        this();
        this.welcomeFrame = welcomeFrame;
        this.filePath = filePath;
        loadProducts();
    }
    private void loadProducts(){
        ProductFileHandler fileHandler = new ProductFileHandler();
        java.util.List<Product> products = fileHandler.readProductFromFile(filePath);
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getIdentifier(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockQuantity()
            });
        }
    }
    private void initializeComponents(){
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        //textFields below
        inputPanel.add(new JLabel("Identifier: "));
        identifierField = new JTextField();
        inputPanel.add(identifierField);

        inputPanel.add(new JLabel("Name: "));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description: "));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Price: "));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Stock Quantity: "));
        stockQuantityField = new JTextField();
        inputPanel.add(stockQuantityField);

        addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addNewProduct());
        removeButton = new JButton("Remove Selected Product");
        removeButton.addActionListener( e -> removeSelectedProduct());
        goBackButton = new JButton("Go Back");
        goBackButton.addActionListener( e -> goBack());

        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(goBackButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

    }
    private void goBack(){
        this.setVisible(false);
        welcomeFrame.setVisible(true);
    }
    private void addNewProduct(){
        String identifier = identifierField.getText();
        String name = nameField.getText();
        String description = descriptionField.getText();
        String priceText = priceField.getText();
        String stockQuantityText = stockQuantityField.getText();

        // check validation of input
        String identifierPattern = "(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]{10}$";


        if(!(identifier.matches(identifierPattern))) {
            JOptionPane.showMessageDialog(this, "Identifier must be 10 characters long and consists of uppercase letters and numbers.", "Invalid Identifier", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this, identifier + ": " +  name + " added successfully!", "Product added successfully!", JOptionPane.INFORMATION_MESSAGE);
        }

        // check price and stockQuantity type.
        double price;
        int stockQuantity;
        try{
            price = Double.parseDouble(priceText);
            stockQuantity = Integer.parseInt(stockQuantityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a number and stock quantity must be an integer.", "Invalid price or stock quantity input.", JOptionPane.ERROR_MESSAGE);
            return;
        }


        tableModel.addRow(new Object[]{identifier, name, description, price, stockQuantity});
        saveProductsToFile();
    }
    private void removeSelectedProduct(){
        int selectedRow = productTable.getSelectedRow();
        if(selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            saveProductsToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select product to remove from the cart.", "No product selected.", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void saveProductsToFile() {
        java.util.ArrayList<Product> products = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            products.add(new Product(
                    (String) tableModel.getValueAt(i, 0), // Identifier
                    (String) tableModel.getValueAt(i, 1), // Name
                    (String) tableModel.getValueAt(i, 2), // Description
                    (double) tableModel.getValueAt(i, 3), // Name
                    (int) tableModel.getValueAt(i, 4)// Name
            ));
        }
        ProductFileHandler fileHandler = new ProductFileHandler();
        fileHandler.writeProductsToFile(filePath, products);
    }


    private java.util.List<Product> getAllProductsFromTable() {
        java.util.List<Product> products = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            // Extract data for each row and create a Product object
            // Add it to the list
            String identifier = (String) tableModel.getValueAt(i, 0); // Identifier
            String name = (String) tableModel.getValueAt(i, 1); // Name
            String description = (String) tableModel.getValueAt(i, 2); // Description
            double price = (Double) tableModel.getValueAt(i, 3); // Price
            int stockQuantity = (Integer) tableModel.getValueAt(i, 4); // Stock Quantity
            products.add(new Product(identifier, name, description, price, stockQuantity));

        }
        return products;
    }
    private void initializeTable(){
        String[] columnNames = {"Identifier", "Name", "Description", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        tableModel.addTableModelListener(e -> {
            // automatically save whenever there are changes. No save button or hotKey needed.
            saveProductsToFile();
        });
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

}
