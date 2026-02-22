package com.shopnest;

import com.shopnest.model.Product;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  Welcome to ShopNest 🛒");
        System.out.println("  Java Full Stack Platform");
        System.out.println("=================================");


        // Create a product
        Product p = new Product("P001", "iPhone 15", "Latest Apple smartphone", 79999.0, 50, "Electronics");

        System.out.println(p);
        System.out.println("Available: " + p.isAvailable());
        System.out.println("Price after 10% discount: " + p.getPriceAfterDiscount(10));

        // Reduce stock
        p.reduceStock(5);
        System.out.println("Stock after order of 5: " + p.getStockQuantity());

        // This should throw an exception — try it!
        // p.reduceStock(1000);
    }
}