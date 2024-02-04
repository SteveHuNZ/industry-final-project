package ictgradschool.industry.final_project;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;

public class InventoryManagerFrame extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String filePath;
    private JTextField identifierField, nameField, descriptionField, priceField, stockQuantityField;
    private JButton addButton, removeButton;
    public InventoryManagerFrame() {
        setTitle("Inventory Manager");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
        initializeTable();
    }
    public InventoryManagerFrame(String filePath){
        this();
        this.filePath = filePath;
        loadProducts();
    }
    private void loadProducts(){
        ProductFileHandler fileHandler = new ProductFileHandler();
        java.util.List<Product> products = fileHandler.readProductFromFile(filePath);
    }
    private void initializeComponents(){
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

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
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

    }
    private void addNewProduct(){
        String identifier = identifierField.getText();
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stockQuantity = Integer.parseInt(stockQuantityField.getText());

        tableModel.addRow(new Object[]{identifier, name, description, price, stockQuantity});
        saveProductsToFile();
    }
    private void removeSelectedProduct(){
        int selectedRow = productTable.getSelectedRow();
        if(selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            saveProductsToFile();
        } else {
            // Maybe show message to user. Later to modify.
        }
    }
    private void saveProductsToFile() {
        java.util.ArrayList<Product> products = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            products.add(new Product(
                    (String) tableModel.getValueAt(i, 0), // Identifier
                    (String) tableModel.getValueAt(i, 1), // Name
                    (String) tableModel.getValueAt(i, 2), // Name
                    (double) tableModel.getValueAt(i, 3), // Name
                    (int) tableModel.getValueAt(i, 4)// Name
            ));
        }
        ProductFileHandler fileHandler = new ProductFileHandler();
        fileHandler.writeProductsToFile(filePath, products);
    }

    /**
     * private void addNewProduct() {
     *     // Get data from text fields...
     *     Product newProduct = new Product(identifier, name, description, price, quantity);
     *     tableModel.addRow(new Object[]{identifier, name, description, price, quantity});
     *     saveProductsToFile();
     * }
     *
     * private void saveProductsToFile() {
     *     List<Product> allProducts = getAllProductsFromTable();
     *     ProductFileHandler fileHandler = new ProductFileHandler();
     *     fileHandler.writeProductsToFile(filePath, allProducts);
     * }
     *
     * private List<Product> getAllProductsFromTable() {
     *     List<Product> products = new ArrayList<>();
     *     for (int i = 0; i < tableModel.getRowCount(); i++) {
     *         // Extract data for each row and create a Product object
     *         // Add it to the list
     *     }
     *     return products;
     * }
     */

    private void initializeTable(){
        String[] columnNames = {"Identifier", "Name", "Description", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
