package ictgradschool.industry.final_project;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.TableRowSorter;

public class InventoryManagerFrame extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String filePath;
    private JTextField identifierField, nameField, descriptionField, priceField, stockQuantityField;
    private JButton addButton, removeButton, goBackButton;
    private WelcomeFrame welcomeFrame;
    private JComboBox<String> stockFilterComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;
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
    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Panel for Add, Remove, and Go Back buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        goBackButton = new JButton("Go Back");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(goBackButton);

        addButton.addActionListener(e -> addNewProduct());
        removeButton.addActionListener(e -> removeSelectedProduct());
        goBackButton.addActionListener(e -> goBack());

        // Panel for Search components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        clearSearchButton = new JButton("Clear Search");
        clearSearchButton.addActionListener(e -> clearSearch());
        stockFilterComboBox = new JComboBox<>(new String[]{"Show All", "In-Stock", "Out-of-Stock"});
        stockFilterComboBox.addActionListener(e -> {
            String filterType = (String) stockFilterComboBox.getSelectedItem();
            switch (filterType){
                case "In-Stock":
                    applyStockFilter("In-Stock");
                    break;
                case "Out-of-Stock":
                    applyStockFilter("Out-of-Stock");
                    break;
                default:
                    clearSearch();
                    break;
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);
        searchPanel.add(stockFilterComboBox);

        searchButton.addActionListener(e -> applySearchFilter(searchField.getText()));

        // Panel for inputs and labels
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
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

        // Adding button panel and search panel to the main panel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(searchPanel, BorderLayout.CENTER);
        southPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }

    public void applySearchFilter(String searchText) {
        RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + searchText);
        // (?!) makes the search case-insensitive(Do not demanding on Uppercase or Lowercase).
        ((TableRowSorter<DefaultTableModel>) productTable.getRowSorter()).setRowFilter(rf);
    }

    // Method to clear search filter and show all items
    public void clearSearch() {
        applySearchFilter("");
    }

    // Method to apply stock quantity filter
    public void applyStockFilter(String filterType) {
        RowFilter<DefaultTableModel, Integer> rf = null;
        if (filterType.equals("In-Stock")) {
            rf = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 0, 4); // Assuming stock quantity is in column 4
        } else if (filterType.equals("Out-of-Stock")) {
            rf = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, 0, 4);
        }
        ((TableRowSorter<DefaultTableModel>) productTable.getRowSorter()).setRowFilter(rf);
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

    private void initializeTable(){
        String[] columnNames = {"Identifier", "Name", "Description", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 3: // price column
                        return Double.class;
                    case 4: // stock quantity column
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };
        tableModel.addTableModelListener(e -> {
            saveProductsToFile();
        });
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);
    }


}
