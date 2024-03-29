package ictgradschool.industry.final_project;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class WelcomeFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel fileChooserPanel;
    private JPanel optionsPanel;
    private JFileChooser fileChooser;
    String filePath;

    public WelcomeFrame(){
        // set up JFrame;
        setTitle("---Welcome to WareMarket Manager System---");
        setSize(1200,750);
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
        JLabel welcomeLabel = new JLabel("<html><h1>Select a file to begin.</h1></html>", JLabel.CENTER);
        fileChooserPanel.add(welcomeLabel, BorderLayout.NORTH);

        // create a JLabel to show image;
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getResource("/WareMarket.png"));
        imageLabel.setIcon(icon);
        fileChooserPanel.add(imageLabel, BorderLayout.CENTER);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JButton openButton = new JButton("Open an existing Filestore");
        JButton newFileButton = new JButton("Create a new file to continue.");
        newFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Specify a file to continue");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TXT Files", "txt"));
                int userSelection = fileChooser.showSaveDialog(WelcomeFrame.this);

                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();
                    if(!fileToSave.getAbsolutePath().endsWith(".txt")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                    }
                    try {
                        if(fileToSave.createNewFile()){
                            filePath = fileToSave.getAbsolutePath();
                            JOptionPane.showMessageDialog(WelcomeFrame.this, "File created: "+ fileToSave.getAbsolutePath());
                            cardLayout.show(mainPanel, "Options");
                        } else {
                            JOptionPane.showMessageDialog(WelcomeFrame.this, "File already exists.", "File Exists.", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex){
                        JOptionPane.showMessageDialog(WelcomeFrame.this, "An error occurred while creating the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
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
        buttonPanel.add(newFileButton);
        fileChooserPanel.add(buttonPanel, BorderLayout.SOUTH);
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
                InventoryManagerFrame inventoryManagerFrame = new InventoryManagerFrame(filePath, WelcomeFrame.this);
                inventoryManagerFrame.setVisible(true);
                WelcomeFrame.this.setVisible(false);
            }
        });

        openPointOfSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductFileHandler fileHandler = new ProductFileHandler();
                java.util.List<Product> inventory =fileHandler.loadProductFromFile(filePath);
                PointOfSaleFrame pointOfSaleFrame = new PointOfSaleFrame(inventory, WelcomeFrame.this);
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
