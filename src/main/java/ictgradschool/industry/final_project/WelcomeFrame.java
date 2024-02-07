package ictgradschool.industry.final_project;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class WelcomeFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel fileChooserPanel;
    private JPanel optionsPanel;
    private JFileChooser fileChooser;
    String filePath;

    public WelcomeFrame(){
        // set up JFrame;
        setTitle("---Welcome---");
        setSize(700,450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Initialize CardLayout and JPanel;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panel;
        initializeFileChooserPanel();
        initializeOptionsPanel();

        // Add panel to main panel;
        mainPanel.add(fileChooserPanel, "File Chooser");
        mainPanel.add(optionsPanel, "Options");

        // add main panel to Frame;
        add(mainPanel);

        // show the file chooser panel firstly;
        cardLayout.show(mainPanel,"File Chooser");
    }

    private void initializeFileChooserPanel(){
        fileChooserPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("<html><h1>Welcome to Inventory Manager</h1><p>Select a file to begin.</p></html>", JLabel.CENTER);
        fileChooserPanel.add(welcomeLabel, BorderLayout.NORTH);


        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JButton openButton = new JButton("Open/Create Filestore");
        openButton.addActionListener(new ActionListener() {
            // actionListener is an interface, to implement it, you must override the method "actionPerformed".
            @Override
            public void actionPerformed(ActionEvent e) {

                int returnVal = fileChooser.showOpenDialog(WelcomeFrame.this);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    // APPROVE_OPTION and returnVal are both integers, and they are final constants which is decided by method "showOpenDialog" and component "JFileChooser".
                    File file = fileChooser.getSelectedFile();
                    filePath = file.getAbsolutePath();

                    cardLayout.show(mainPanel, "Options");
                    // cardLayout is just like a bunch of cards which you can check only one of them at the same time.

                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        fileChooserPanel.add(buttonPanel, BorderLayout.SOUTH);
        fileChooserPanel.add(fileChooser, BorderLayout.CENTER);
    }
    private void initializeOptionsPanel(){
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));

        JButton closeFilestoreButton = new JButton("Close Filestore");
        JButton openInventoryManagerButton = new JButton("Open Inventory Manager");
        JButton openPointOfSaleButton = new JButton("Open Point of Sale");

        openInventoryManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryManagerFrame inventoryManagerFrame = new InventoryManagerFrame(filePath);
                inventoryManagerFrame.setVisible(true);
                WelcomeFrame.this.setVisible(false);
            }
        });

        openPointOfSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductFileHandler fileHandler = new ProductFileHandler();
                java.util.List<Product> inventory =fileHandler.loadProductFromFile(filePath);
                PointOfSaleFrame pointOfSaleFrame = new PointOfSaleFrame(inventory);
                pointOfSaleFrame.setVisible(true);
                WelcomeFrame.this.setVisible(false);
            }
        });
        closeFilestoreButton.addActionListener(e -> cardLayout.show(mainPanel, "File Chooser"));

        optionsPanel.add(closeFilestoreButton);
        optionsPanel.add(openInventoryManagerButton);
        optionsPanel.add(openPointOfSaleButton);
    }
}
