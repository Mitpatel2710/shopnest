package com.shopnest.model;

public enum PriceRange {
    BUDGET      ("Budget",    0,      2000),
    MID_RANGE   ("Mid Range", 2000,   10000),
    PREMIUM     ("Premium",   10000,  50000),
    LUXURY      ("Luxury",    50000,  Double.MAX_VALUE);

    private final String label;
    private final double min;
    private final double max;

    PriceRange(String label, double min, double max) {
        this.label = label;
        this.min   = min;
        this.max   = max;
    }

    public String getLabel() { return label; }

    // Find which range a price falls into
    public static PriceRange of(double price) {
        for (PriceRange range : values()) {
            if (price >= range.min && price < range.max) return range;
        }
        return LUXURY;
    }
}