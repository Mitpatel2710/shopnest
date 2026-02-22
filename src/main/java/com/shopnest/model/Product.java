package com.shopnest.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product {

    //Fields
    private final String id;                        // final = immutable once set
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;
    private String imageUrl;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor (full)
    public Product(String id, String name,String description,
                   double price, int stockQuantity, String category) {
        if(id == null || id.isBlank())          throw new IllegalArgumentException("Product ID cannot be empty");
        if (name == null || name.isBlank())     throw new IllegalArgumentException("Product name cannot be empty");
        if (price < 0)                          throw new IllegalArgumentException("Price cannot be negative");
        if (stockQuantity < 0)                  throw new IllegalArgumentException("Stock cannot be negative");

        this.id                     = id;
        this.name                   = name;
        this.description            = description;
        this.price                  = price;
        this.stockQuantity          = stockQuantity;
        this.category               = category;
        this.active                 = true;
        this.createdAt              = LocalDateTime.now();
        this.updatedAt              = LocalDateTime.now();

    }

    // Business Methods

    // Is product available to buy?
    public boolean isAvailable (){
        return active && stockQuantity >0;
    }

    // Reduce stock when someone places an order
    public void reduceStock(int quantity){
        if(quantity <= 0)      throw new IllegalArgumentException("Quantity must be positive");
        if(quantity > this.stockQuantity) throw new IllegalArgumentException("Not enough stock. Available: " + this.stockQuantity);
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Restock the product
    public void addStock(int quantity){
        if(quantity <= 0)      throw new IllegalArgumentException("Quantity must be positive");
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Apply discount and return discounted price (doesn't change original)
    public double getPriceAfterDiscount(double discountedPercent){
        if(discountedPercent < 0 || discountedPercent > 100)
            throw new IllegalArgumentException("Discount must be between O and 100");
        return this.price - (this.price * discountedPercent/100);
    }

    // Getters


    public String getId() {
        return id;
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

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters (only mutable fields)
    public void setName (String name){
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPrice(double price) {
        if(price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCategory(String category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    // equals & hashCode (based on ID only)
    @Override
    public boolean equals (Object o){
        if(this == o) return true;
        if(!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id,product.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stockQuantity +
                ", category='" + category + '\'' +
                ", active=" + active +
                '}';
    }
}
