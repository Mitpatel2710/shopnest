package com.shopnest.model;

public class CartItem {
    private final BaseProduct product;  // reference to live product
    private int quantity;

    public CartItem(BaseProduct product, int quantity) {
        if (product == null)  throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0)    throw new IllegalArgumentException("Quantity must be positive");
        if (!product.isAvailable()) throw new IllegalArgumentException("Product is not available");

        this.product  = product;
        this.quantity = quantity;
    }

    // ── Business Methods ──────────────────────────────────
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (this.quantity + amount > product.getStockQuantity())
            throw new IllegalArgumentException("Not enough stock. Available: " + product.getStockQuantity());
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (amount <= 0)          throw new IllegalArgumentException("Amount must be positive");
        if (amount >= this.quantity) throw new IllegalArgumentException("Cannot decrease below 1. Remove item instead.");
        this.quantity -= amount;
    }

    // ── Getters ───────────────────────────────────────────
    public BaseProduct getProduct() { return product; }
    public int getQuantity()        { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{product='" + product.getName() +
                "', qty=" + quantity +
                ", subtotal=₹" + getSubtotal() + "}";
    }
}
