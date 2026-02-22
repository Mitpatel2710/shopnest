package com.shopnest.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    private final String id;
    private final User user;                    // Order belongs to User
    private final List<OrderItem> orderItems;   // snapshot of cart at checkout
    private OrderStatus status;
    private final double totalAmount;
    private String deliveryAddress;
    private String paymentMethod;
    private final LocalDateTime placedAt;
    private LocalDateTime updatedAt;

    // ── Built from a Cart ─────────────────────────────────
    public Order(String id, User user, Cart cart, String deliveryAddress) {
        if (id == null || id.isBlank())           throw new IllegalArgumentException("Order ID cannot be empty");
        if (user == null)                         throw new IllegalArgumentException("User cannot be null");
        if (cart == null || cart.isEmpty())       throw new IllegalArgumentException("Cannot place order from empty cart");
        if (deliveryAddress == null || deliveryAddress.isBlank()) throw new IllegalArgumentException("Delivery address required");

        this.id              = id;
        this.user            = user;
        this.deliveryAddress = deliveryAddress;
        this.status          = OrderStatus.PENDING;
        this.placedAt        = LocalDateTime.now();
        this.updatedAt       = LocalDateTime.now();

        // Convert CartItems → OrderItems (snapshot)
        this.orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            this.orderItems.add(new OrderItem(cartItem));
            // Reduce stock for each product
            cartItem.getProduct().reduceStock(cartItem.getQuantity());
        }

        // Calculate total once and lock it
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    // ── Business Methods ──────────────────────────────────
    public void confirm() {
        if (status != OrderStatus.PENDING)
            throw new IllegalStateException("Only PENDING orders can be confirmed");
        this.status    = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship() {
        if (status != OrderStatus.CONFIRMED && status != OrderStatus.PROCESSING)
            throw new IllegalStateException("Order must be CONFIRMED before shipping");
        this.status    = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED)
            throw new IllegalStateException("Order must be SHIPPED before delivery");
        this.status    = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED)
            throw new IllegalStateException("Cannot cancel order that is already " + status);
        this.status    = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCancellable() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    // ── Getters ───────────────────────────────────────────
    public String getId()                     { return id; }
    public User getUser()                     { return user; }
    public List<OrderItem> getOrderItems()    { return Collections.unmodifiableList(orderItems); }
    public OrderStatus getStatus()            { return status; }
    public double getTotalAmount()            { return totalAmount; }
    public String getDeliveryAddress()        { return deliveryAddress; }
    public String getPaymentMethod()          { return paymentMethod; }
    public LocalDateTime getPlacedAt()        { return placedAt; }
    public LocalDateTime getUpdatedAt()       { return updatedAt; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    // ── equals, hashCode, toString ────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Order{id='" + id +
                "', user='" + user.getFullName() +
                "', items=" + orderItems.size() +
                "', status=" + status +
                ", total=₹" + totalAmount + "}";
    }
}
