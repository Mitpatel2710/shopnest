package com.shopnest.model;

public class OrderItem {

    // ⚠️ Key concept — we COPY product details, not reference
    // If product price changes tomorrow, this order stays accurate
    private final String productId;
    private final String productName;
    private final double priceAtPurchase;   // snapshot of price when ordered
    private final int quantity;

    // ── Created from a CartItem ────────────────────────────
    public OrderItem(CartItem cartItem) {
        this.productId       = cartItem.getProduct().getId();
        this.productName     = cartItem.getProduct().getName();
        this.priceAtPurchase = cartItem.getProduct().getPrice();
        this.quantity        = cartItem.getQuantity();
    }

    // ── Business Methods ──────────────────────────────────
    public double getSubtotal() {
        return priceAtPurchase * quantity;
    }

    // ── Getters ───────────────────────────────────────────
    public String getProductId()        { return productId; }
    public String getProductName()      { return productName; }
    public double getPriceAtPurchase()  { return priceAtPurchase; }
    public int getQuantity()            { return quantity; }

    @Override
    public String toString() {
        return "OrderItem{product='" + productName +
                "', qty=" + quantity +
                ", price=₹" + priceAtPurchase +
                ", subtotal=₹" + getSubtotal() + "}";
    }
}
