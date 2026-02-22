package com.shopnest.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Cart {
    private final String id;
    private final User user;                    // Cart belongs to a User — composition
    private final List<CartItem> items;         // Cart HAS many CartItems
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart(String id, User user) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Cart ID cannot be empty");
        if (user == null)               throw new IllegalArgumentException("User cannot be null");

        this.id        = id;
        this.user      = user;
        this.items     = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ── Business Methods ──────────────────────────────────

    public void addItem(BaseProduct product, int quantity) {
        if (product == null)  throw new IllegalArgumentException("Product cannot be null");  // ✅ check null FIRST
        if (quantity <= 0)    throw new IllegalArgumentException("Quantity must be positive");

        // If product already in cart — increase quantity
        Optional<CartItem> existing = findItem(product.getId());
        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(String productId) {
        boolean removed = items.removeIf(item -> item.getProduct().getId().equals(productId));
        if (!removed) throw new IllegalArgumentException("Product not found in cart: " + productId);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateQuantity(String productId, int newQuantity) {
        CartItem item = findItem(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart: " + productId));
        item.setQuantity(newQuantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void clear() {
        items.clear();
        this.updatedAt = LocalDateTime.now();
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // ── Private helper ────────────────────────────────────
    private Optional<CartItem> findItem(String productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    // ── Getters ───────────────────────────────────────────
    public String getId()                  { return id; }
    public User getUser()                  { return user; }
    public List<CartItem> getItems()       { return Collections.unmodifiableList(items); }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public LocalDateTime getUpdatedAt()    { return updatedAt; }

    @Override
    public String toString() {
        return "Cart{id='" + id + "', user='" + user.getFullName() +
                "', items=" + items.size() +
                ", total=₹" + getTotalPrice() + "}";
    }
}
