package ictgradschool.industry.final_project;

import javax.swing.*;
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

    public WelcomeFrame(){
        // set up JFrame;
        setTitle("Welcome to management system");
        setSize(400,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Initialize CardLayout and JPanel;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panel;
        initializeFileChooserPanel();
        initializeOptionsPanel();

        // Add panel to main panel;
        mainPanel.add(fileChooserPanel, "File chooser");
        mainPanel.add(optionsPanel, "Options");

        // add main panel to Frame;
        add(mainPanel);

        // show the file chooser panel firstly;
        cardLayout.show(mainPanel,"File Chooser");
    }

    private void initializeFileChooserPanel(){
        fileChooserPanel = new JPanel();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JButton openButton = new JButton("Open/Create Filestore");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(WelcomeFrame.this);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();

                    cardLayout.show(mainPanel, "Options");
                }
            }
        });
        fileChooserPanel.add(fileChooser);
        fileChooserPanel.add(openButton);
    }
    private void initializeOptionsPanel(){
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));

        JButton closeFilestoreButton = new JButton("Close Filestore");
        JButton openInventoryManagerButton = new JButton("Open Inventory Manager");
        JButton openPointOfSaleButton = new JButton("Open Point of Sale");

        closeFilestoreButton.addActionListener(e -> cardLayout.show(mainPanel, "File Chooser"));

        optionsPanel.add(closeFilestoreButton);
        optionsPanel.add(openInventoryManagerButton);
        optionsPanel.add(openPointOfSaleButton);
    }
}
