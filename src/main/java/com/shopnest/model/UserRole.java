package com.shopnest.model;

public enum UserRole {
    CUSTOMER,           // can browse, add to cart, place orders
    SELLER,             // can list products, manage inventory
    ADMIN               // full access - manage users, products, orders
}
