package ictgradschool.industry.final_project;
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
            e.printStackTrace();
        }
        return products;
    }
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
            e.printStackTrace();
        }
    }
    private String productToFileLine(Product product){
        return product.getIdentifier() + "," + product.getName() + "," + product.getDescription()
                + "," + product.getPrice() + "," + product.getStockQuantity() + ",";
    }
}
