package com.shopnest.model;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseProduct implements Discountable {

    // Fields (protected = accessible to subclasses)
    protected final String id;
    protected String name;
    protected String description;
    protected double price;
    protected int stockQuantity;
    protected String category;
    protected boolean active;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseProduct(String id, String name, String description,
                       double price, int stockQuantity, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Abstract method — subclasses MUST implement
    // Each product type describes itself differently
    public abstract String getProductDetails();

    //Concrete methods - shared by all products
    public boolean isAvailable(){
        return active && stockQuantity > 0;
    }

    public void reduceStock(int quantity){
        if(quantity <= 0)                   throw new IllegalArgumentException("Quantity must be positive");
        if(quantity > this.stockQuantity)   throw new IllegalArgumentException("Not enough stock. Available: " + this.stockQuantity);
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void addStock(int quantity){
        if(quantity <= 0)                throw new IllegalArgumentException("Quantity must be positive");
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Implementing Discountable interface
    @Override
    public double getPriceAfterDiscount(double discountPercent) {
        if (discountPercent < 0 || discountPercent > MAX_DISCOUNT_PERCENT)
            throw new IllegalArgumentException("Discount must be between 0 and " + MAX_DISCOUNT_PERCENT);
        return this.price - (this.price * discountPercent / 100);
    }

    //Getters

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

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setName(String name){
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPrice(double price){
        if(price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof BaseProduct)) return false;
        BaseProduct that = (BaseProduct) o;
        return Objects.equals(id,that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseProduct{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stockQuantity +
                '}';
    }
}
