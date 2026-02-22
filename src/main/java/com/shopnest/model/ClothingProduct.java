package com.shopnest.model;

public class ClothingProduct extends BaseProduct {
    public enum Size { XS, S,M,L,XL,XXL}

    //Clothing-specific fields
    private String brand;
    private Size size;
    private String color;
    private String material;

    public ClothingProduct(String id, String name, String description,
                           double price, int stockQuantity,
                           String brand, Size size, String color) {
        super(id, name, description, price, stockQuantity, "Clothing");
        this.brand = brand;
        this.size = size;
        this.color = color;
    }

    // ── Must implement abstract method from BaseProduct ───
    @Override
    public String getProductDetails() {
        return String.format("Clothing | %s | Brand: %s | Size: %s | Color: %s",
                name, brand, size, color);
    }

    // ── Clothing is always eligible for seasonal discounts ─
    @Override
    public boolean isEligibleForDiscount() {
        return true;
    }

    // ── Getters / Setters ─────────────────────────────────
    public String getBrand()            { return brand; }
    public Size getSize()               { return size; }
    public String getColor()            { return color; }
    public String getMaterial()         { return material; }
    public void setSize(Size size)      { this.size = size; }
    public void setColor(String color)  { this.color = color; }
    public void setMaterial(String m)   { this.material = m; }

    @Override
    public String toString() {
        return "ClothingProduct{id='" + id + "', name='" + name +
                "', brand='" + brand + "', size=" + size +
                "', color='" + color + "', price=" + price + "}";
    }
}
