package com.shopnest.util;

// Singleton Pattern — only ONE instance exists in the entire app
// Spring beans are Singletons by default — this is how it works under the hood
public class AppConfig {

    // ── Thread-safe Singleton using enum (best practice) ──
    // Enum guarantees single instance even with reflection and serialization
    private static AppConfig instance;
    private static final Object LOCK = new Object();

    // App configuration values
    private final String appName;
    private final String version;
    private final String currency;
    private final int maxCartItems;
    private final double maxDiscountPercent;
    private final int defaultPageSize;

    // ── Private constructor — nobody can call new AppConfig() ──
    private AppConfig() {
        this.appName            = "ShopNest";
        this.version            = "1.0.0";
        this.currency           = "INR";
        this.maxCartItems       = 20;
        this.maxDiscountPercent = 70.0;
        this.defaultPageSize    = 10;
    }

    // ── Double-checked locking — thread safe getInstance() ──
    public static AppConfig getInstance() {
        if (instance == null) {                     // first check — no sync overhead
            synchronized (LOCK) {
                if (instance == null) {             // second check — inside lock
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    // ── Getters ───────────────────────────────────────────
    public String getAppName()             { return appName; }
    public String getVersion()             { return version; }
    public String getCurrency()            { return currency; }
    public int getMaxCartItems()           { return maxCartItems; }
    public double getMaxDiscountPercent()  { return maxDiscountPercent; }
    public int getDefaultPageSize()        { return defaultPageSize; }

    // Prevent cloning — would break Singleton
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone AppConfig singleton");
    }

    @Override
    public String toString() {
        return "AppConfig{app='" + appName + "', version='" + version +
                "', currency='" + currency + "', maxCart=" + maxCartItems + "}";
    }
}