package ictgradschool.industry.final_project;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;

public class InventoryManagerFrame extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public InventoryManagerFrame() {
        setTitle("Inventory Manager");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
    }
    private void initializeComponents(){
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
    }
    private void initializeTable(){
        String[] columnNames = {"Identifier", "Name", "Description", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
