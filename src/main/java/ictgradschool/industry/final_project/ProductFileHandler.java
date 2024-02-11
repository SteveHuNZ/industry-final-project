package ictgradschool.industry.final_project;
import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ProductFileHandler {
    // read from file
    public List<Product> readProductFromFile(String filePath){
        List<Product> products = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                Product product = parseProductLine(line);
                products.add(product);
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Failed to read date from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }
    /**
     //TODO: the method below has never be used. Check later. Maybe delete it.
     public void addProductToFile(String productInfo, String filePath) {
     BufferedWriter bw = null;
     try {
     // create BufferedWriter
     bw = new BufferedWriter(new FileWriter(filePath, true));
     bw.write(productInfo);
     bw.newLine(); // add a new line
     bw.flush(); // add buffer, make sure write data into file.
     } catch (IOException e) {
     e.printStackTrace();
     } finally {
     if (bw != null) {
     try {
     bw.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }
     }
     }
     */
    private Product parseProductLine(String line){
        String[] items = line.split(",");
        String identifier = items[0];
        String name = items[1];
        String description = items[2];
        double price = Double.parseDouble(items[3]);
        int stockQuantity = Integer.parseInt(items[4]);
        return new Product(identifier, name, description, price, stockQuantity);
    }
    // write to file
    public void writeProductsToFile(String filePath, List<Product> products) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for(Product p : products) {
                writer.write(productToFileLine(p));
                writer.newLine();
            }
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Failed to write date to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private String productToFileLine(Product product){
        return product.getIdentifier() + "," + product.getName() + "," + product.getDescription()
                + "," + product.getPrice() + "," + product.getStockQuantity() + ",";
    }
    public void writeFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to write to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public List<Product> loadProductFromFile(String filePath){
        List<Product> products = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null){
                String[] items = line.split(",");
                if(items.length >= 5){
                    String identifier = items[0].trim();
                    String name = items[1].trim();
                    String description = items[2].trim();
                    double price = Double.parseDouble(items[3].trim());
                    int stockQuantity = Integer.parseInt(items[4].trim());
                    products.add(new Product(identifier, name, description, price, stockQuantity));
                }
            }
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Failed to load product from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }
}
