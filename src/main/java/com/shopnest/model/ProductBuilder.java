package com.shopnest.model;

import com.shopnest.exception.InvalidProductException;
import java.util.ArrayList;
import java.util.List;

// Builder Pattern — construct Product step by step
// Avoids telescoping constructors and parameter confusion
public class ProductBuilder {

    // Required fields
    private String id;
    private String name;
    private double price;
    private int stockQuantity;
    private String category;

    // Optional fields — have sensible defaults
    private String description  = "";
    private String imageUrl     = "";
    private boolean active      = true;

    // ── Fluent setters — each returns 'this' for chaining ──
    public ProductBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder stock(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }

    public ProductBuilder category(String category) {
        this.category = category;
        return this;
    }

    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ProductBuilder inactive() {
        this.active = false;
        return this;
    }

    // ── Build — validate then construct ───────────────────
    public Product build() {
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            throw new InvalidProductException(id != null ? id : "UNKNOWN", errors);
        }
        Product product = new Product(id, name, description, price, stockQuantity, category);
        product.setImageUrl(imageUrl);
        if (!active) product.setActive(false);
        return product;
    }

    // ── Validate all fields before building ───────────────
    private List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (id == null || id.isBlank())       errors.add("ID is required");
        if (name == null || name.isBlank())   errors.add("Name is required");
        if (price < 0)                        errors.add("Price cannot be negative");
        if (stockQuantity < 0)                errors.add("Stock cannot be negative");
        if (category == null || category.isBlank()) errors.add("Category is required");
        return errors;
    }

    // ── Static entry point — ProductBuilder.newProduct() ──
    public static ProductBuilder newProduct() {
        return new ProductBuilder();
    }
}