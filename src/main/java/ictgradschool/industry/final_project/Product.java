package ictgradschool.industry.final_project;

public class Product {
    private String identifier;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    public Product(String identifier, String name, String description, double price, int stockQuantity) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    @Override
    public String toString(){
        return name + " - $" + price + ". Description: " + description;
    }

}
