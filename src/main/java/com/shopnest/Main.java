package com.shopnest;

import com.shopnest.model.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("  Welcome to ShopNest 🛒");
        System.out.println("  Java Full Stack Platform");
        System.out.println("=================================\n");

        // ──────────────────────────────────────────────────
        // EC-002 — Product class (basic OOP)
        // ──────────────────────────────────────────────────
        System.out.println("===== EC-002: Product Class =====");

        Product p = new Product("P001", "iPhone 15", "Latest Apple smartphone", 79999.0, 50, "Electronics");

        System.out.println(p);
        System.out.println("Available: " + p.isAvailable());
        System.out.println("Price after 10% discount: " + p.getPriceAfterDiscount(10));

        p.reduceStock(5);
        System.out.println("Stock after order of 5: " + p.getStockQuantity());

        // Uncomment to test exception:
        // p.reduceStock(1000);

        // ──────────────────────────────────────────────────
        // EC-003 — Inheritance & Interfaces
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-003: Inheritance & Interfaces =====");

        // Polymorphism — BaseProduct reference, child object
        BaseProduct laptop = new ElectronicsProduct(
                "E001", "MacBook Pro", "Apple M3 chip",
                120000.0, 10, "Apple", 12
        );

        BaseProduct tshirt = new ClothingProduct(
                "C001", "Nike Dri-FIT Tee", "Sports t-shirt",
                1499.0, 100, "Nike", ClothingProduct.Size.L, "Black"
        );

        // Same method call — different output (polymorphism)
        System.out.println(laptop.getProductDetails());
        System.out.println(tshirt.getProductDetails());

        // Discount eligibility
        System.out.println("\nDiscount eligible?");
        System.out.println("Laptop: " + laptop.isEligibleForDiscount());
        System.out.println("T-Shirt: " + tshirt.isEligibleForDiscount());

        // Shippable — only ElectronicsProduct implements it
        ElectronicsProduct phone = new ElectronicsProduct(
                "E002", "iPhone 15", "Latest Apple phone",
                79999.0, 25, "Apple", 12
        );
        System.out.println("\nShipping cost: ₹" + phone.calculateShippingCost());
        System.out.println("Special packaging: " + phone.requiresSpecialPackaging());
        System.out.println("Delivery days: " + phone.getEstimatedDeliveryDays());

        // instanceof check
        System.out.println("\nIs laptop Shippable? " + (laptop instanceof Shippable));
        System.out.println("Is tshirt Shippable? " + (tshirt instanceof Shippable));


        // ──────────────────────────────────────────────────
        // EC-004 — User, Cart and Order
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-004: User, Cart and Order =====");

        // Create a user
        User user = new User("U001", "Rahul", "Sharma", "rahul@shopnest.com", "secure123");
        System.out.println("User: " + user);
        System.out.println("Role: " + user.getRole());

        // Create products
        ElectronicsProduct macbook = new ElectronicsProduct(
                "E003", "MacBook Air", "M2 chip laptop",
                89999.0, 20, "Apple", 12
        );
        ClothingProduct jeans = new ClothingProduct(
                "C002", "Levi's 501", "Classic straight jeans",
                3499.0, 50, "Levi's", ClothingProduct.Size.M, "Blue"
        );

        // Add to cart
        Cart cart = new Cart("CART001", user);
        cart.addItem(macbook, 1);
        cart.addItem(jeans, 2);

        System.out.println("\nCart: " + cart);
        cart.getItems().forEach(item -> System.out.println("  " + item));
        System.out.println("Cart total: ₹" + cart.getTotalPrice());

        // Add same product again — should increase quantity not duplicate
        cart.addItem(jeans, 1);
        System.out.println("\nAfter adding jeans again:");
        cart.getItems().forEach(item -> System.out.println("  " + item));

        // Place an order
        Order order = new Order("ORD001", user, cart, "123, MG Road, Bengaluru - 560001");
        order.setPaymentMethod("UPI");
        System.out.println("\nOrder placed: " + order);
        order.getOrderItems().forEach(item -> System.out.println("  " + item));

        // Order lifecycle
        System.out.println("\nOrder lifecycle:");
        System.out.println("Status: " + order.getStatus());
        order.confirm();
        System.out.println("After confirm: " + order.getStatus());
        order.ship();
        System.out.println("After ship: " + order.getStatus());
        order.deliver();
        System.out.println("After deliver: " + order.getStatus());

        // Try to cancel delivered order — should throw exception
        try {
            order.cancel();
        } catch (IllegalStateException e) {
            System.out.println("\nExpected error: " + e.getMessage());
        }

        // Promote user role
        user.promoteToSeller();
        System.out.println("\nAfter promotion: " + user.getRole());
    }
}