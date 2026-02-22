package com.shopnest.model;

public class ElectronicsProduct extends BaseProduct implements Shippable {

    // Electronics-specific fields
    private String brand;
    private int warrantyMonths;
    private String modelNumber;

    public ElectronicsProduct(String id, String name, String description,
                              double price, int stockQuantity,
                              String brand, int warrantyMonths) {
        super(id, name, description, price, stockQuantity, "Electronics");
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getProductDetails(){
        return String.format("Electronics | %s | Brand: %s | Warranty: %d months",
                name, brand, warrantyMonths);
    }

    // Implementing Shippable interface
    @Override
    public double calculateShippingCost() {
        // Electronics over ₹10,000 get free shipping
        return price > 10000 ? 0.0 : 99.0;
    }

    @Override
    public boolean requiresSpecialPackaging() {
        // Electronics always need special packaging
        return true;
    }

    @Override
    public int getEstimatedDeliveryDays() {
        // Electronics take longer — fragile items
        return 3;
    }

    // ── Override discount eligibility ─────────────────────
    @Override
    public boolean isEligibleForDiscount() {
        // Only eligible if warranty > 6 months
        return warrantyMonths > 6;
    }

    // ── Getters / Setters ─────────────────────────────────
    public String getBrand()            { return brand; }
    public int getWarrantyMonths()      { return warrantyMonths; }
    public String getModelNumber()      { return modelNumber; }
    public void setBrand(String brand)  { this.brand = brand; }
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }

    @Override
    public String toString() {
        return "ElectronicsProduct{id='" + id + "', name='" + name +
                "', brand='" + brand + "', price=" + price +
                ", warranty=" + warrantyMonths + " months}";
    }
}
