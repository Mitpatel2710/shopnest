package com.shopnest;

import com.shopnest.model.*;
import com.shopnest.service.ProductCatalog;
import com.shopnest.util.ApiResponse;
import com.shopnest.util.Page;
import com.shopnest.util.PaginationUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("  Welcome to ShopNest 🛒");
        System.out.println("  Java Full Stack Platform");
        System.out.println("=================================\n");

        // ──────────────────────────────────────────────────
        // EC-002 — Product Class
        // ──────────────────────────────────────────────────
        System.out.println("===== EC-002: Product Class =====");

        Product p = new Product("P001", "iPhone 15", "Latest Apple smartphone", 79999.0, 50, "Electronics");

        System.out.println(p);
        System.out.println("Available: " + p.isAvailable());
        System.out.println("Price after 10% discount: " + p.getPriceAfterDiscount(10));

        p.reduceStock(5);
        System.out.println("Stock after order of 5: " + p.getStockQuantity());

        // ──────────────────────────────────────────────────
        // EC-003 — Inheritance & Interfaces
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-003: Inheritance & Interfaces =====");

        BaseProduct laptop = new ElectronicsProduct(
                "E001", "MacBook Pro", "Apple M3 chip",
                120000.0, 10, "Apple", 12
        );
        BaseProduct tshirt = new ClothingProduct(
                "C001", "Nike Dri-FIT Tee", "Sports t-shirt",
                1499.0, 100, "Nike", ClothingProduct.Size.L, "Black"
        );

        System.out.println(laptop.getProductDetails());
        System.out.println(tshirt.getProductDetails());

        System.out.println("\nDiscount eligible?");
        System.out.println("Laptop: " + laptop.isEligibleForDiscount());
        System.out.println("T-Shirt: " + tshirt.isEligibleForDiscount());

        ElectronicsProduct phone = new ElectronicsProduct(
                "E002", "iPhone 15", "Latest Apple phone",
                79999.0, 25, "Apple", 12
        );
        System.out.println("\nShipping cost: ₹" + phone.calculateShippingCost());
        System.out.println("Special packaging: " + phone.requiresSpecialPackaging());
        System.out.println("Delivery days: " + phone.getEstimatedDeliveryDays());

        System.out.println("\nIs laptop Shippable? " + (laptop instanceof Shippable));
        System.out.println("Is tshirt Shippable? " + (tshirt instanceof Shippable));

        // ──────────────────────────────────────────────────
        // EC-004 — User, Cart and Order
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-004: User, Cart and Order =====");

        User user = new User("U001", "Rahul", "Sharma", "rahul@shopnest.com", "secure123");
        System.out.println("User: " + user);
        System.out.println("Role: " + user.getRole());

        ElectronicsProduct macbook = new ElectronicsProduct(
                "E003", "MacBook Air", "M2 chip laptop",
                89999.0, 20, "Apple", 12
        );
        ClothingProduct jeans = new ClothingProduct(
                "C002", "Levi's 501", "Classic straight jeans",
                3499.0, 50, "Levi's", ClothingProduct.Size.M, "Blue"
        );

        Cart cart = new Cart("CART001", user);
        cart.addItem(macbook, 1);
        cart.addItem(jeans, 2);

        System.out.println("\nCart: " + cart);
        cart.getItems().forEach(item -> System.out.println("  " + item));
        System.out.println("Cart total: ₹" + cart.getTotalPrice());

        cart.addItem(jeans, 1);
        System.out.println("\nAfter adding jeans again:");
        cart.getItems().forEach(item -> System.out.println("  " + item));

        Order order = new Order("ORD001", user, cart, "123, MG Road, Bengaluru - 560001");
        order.setPaymentMethod("UPI");
        System.out.println("\nOrder placed: " + order);
        order.getOrderItems().forEach(item -> System.out.println("  " + item));

        System.out.println("\nOrder lifecycle:");
        System.out.println("Status: " + order.getStatus());
        order.confirm();
        System.out.println("After confirm: " + order.getStatus());
        order.ship();
        System.out.println("After ship: " + order.getStatus());
        order.deliver();
        System.out.println("After deliver: " + order.getStatus());

        try {
            order.cancel();
        } catch (IllegalStateException e) {
            System.out.println("\nExpected error: " + e.getMessage());
        }

        user.promoteToSeller();
        System.out.println("\nAfter promotion: " + user.getRole());

        // ──────────────────────────────────────────────────
        // EC-005 — Collections Deep Dive
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-005: Collections Deep Dive =====");

        // ⚠️ catalog declared here at method level — accessible to EC-006 too
        ProductCatalog catalog = new ProductCatalog();

        catalog.addProduct(new ElectronicsProduct("E004", "Samsung TV",       "4K QLED TV",          65000.0, 15, "Samsung", 24));
        catalog.addProduct(new ElectronicsProduct("E005", "Sony Headphones",  "Noise cancelling",    12000.0, 30, "Sony",    12));
        catalog.addProduct(new ElectronicsProduct("E006", "iPad Pro",         "M2 chip tablet",      75000.0, 10, "Apple",   12));
        catalog.addProduct(new ClothingProduct("C003", "Adidas Hoodie",   "Warm fleece hoodie",  2999.0,  40, "Adidas", ClothingProduct.Size.L,  "Grey"));
        catalog.addProduct(new ClothingProduct("C004", "Zara Shirt",      "Casual linen shirt",  1799.0,  60, "Zara",   ClothingProduct.Size.M,  "White"));
        catalog.addProduct(new ClothingProduct("C005", "H&M Jacket",      "Winter jacket",       4599.0,  25, "H&M",    ClothingProduct.Size.XL, "Black"));

        System.out.println("\nCatalog: " + catalog);

        System.out.println("\n--- All Products (ArrayList) ---");
        catalog.getAllProducts().forEach(p2 ->
                System.out.println("  " + p2.getId() + " | " + p2.getName() + " | ₹" + p2.getPrice()));

        System.out.println("\n--- HashMap: Find by ID ---");
        catalog.findById("E005").ifPresent(prod -> System.out.println("  Found: " + prod.getName()));
        catalog.findById("INVALID").ifPresentOrElse(
                prod -> System.out.println("  Found: " + prod),
                ()   -> System.out.println("  Product not found — Optional handled safely")
        );

        System.out.println("\n--- LinkedHashMap: Browse by Category ---");
        catalog.getAllCategories().forEach(cat -> {
            List<BaseProduct> products = catalog.findByCategory(cat);
            System.out.println("  " + cat + " (" + products.size() + " products)");
        });

        System.out.println("\n--- Search: 'noise' ---");
        catalog.search("noise").forEach(prod -> System.out.println("  " + prod.getName()));

        System.out.println("\n--- Price Range: ₹1000 - ₹15000 ---");
        catalog.findByPriceRange(1000, 15000)
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\n--- Sorted by Price (Low to High) ---");
        catalog.getSortedByPrice(true)
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\n--- Sorted by Price (High to Low) ---");
        catalog.getSortedByPrice(false)
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\n--- HashSet: Featured Products ---");
        catalog.markAsFeatured("E004");
        catalog.markAsFeatured("C003");
        catalog.markAsFeatured("E004");
        System.out.println("  Featured count: " + catalog.getFeaturedProducts().size());
        catalog.getFeaturedProducts().forEach(prod -> System.out.println("  ⭐ " + prod.getName()));
        System.out.println("  Is E004 featured? " + catalog.isFeatured("E004"));

        System.out.println("\n--- Queue: Order Processing (FIFO) ---");
        User user2 = new User("U002", "Priya", "Patel", "priya@shopnest.com", "pass123");
        Cart cart2 = new Cart("CART002", user2);
        cart2.addItem(catalog.findById("E004").get(), 1);

        Order order1 = new Order("ORD002", user,  cart,  "Mumbai - 400001");
        Order order2 = new Order("ORD003", user2, cart2, "Delhi - 110001");

        catalog.enqueueOrder(order1);
        catalog.enqueueOrder(order2);
        System.out.println("  Orders in queue: " + catalog.getOrderQueueSize());

        catalog.peekNextOrder().ifPresent(o ->
                System.out.println("  Next to process: " + o.getId()));

        while (catalog.getOrderQueueSize() > 0) {
            catalog.processNextOrder().ifPresent(o -> {
                o.confirm();
                System.out.println("  Processed: " + o.getId() + " → " + o.getStatus());
            });
        }

        System.out.println("\n--- TreeMap: Category Stats (Sorted A-Z) ---");
        catalog.getCategoryProductCount()
                .forEach((cat, count) -> System.out.println("  " + cat + ": " + count + " products"));

        System.out.println("\n--- Aggregates ---");
        catalog.getMostExpensive().ifPresent(prod -> System.out.println("  Most expensive: " + prod.getName() + " ₹" + prod.getPrice()));
        catalog.getCheapest().ifPresent(prod ->      System.out.println("  Cheapest: "       + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\n--- Average Price by Category ---");
        catalog.getAveragePriceByCategory()
                .forEach((cat, avg) -> System.out.printf("  %s: ₹%.2f avg%n", cat, avg));

        // ──────────────────────────────────────────────────
        // EC-006 — Generics
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-006: Generics =====");

        System.out.println("\n--- ApiResponse<T> ---");

        // T = BaseProduct
        ApiResponse<BaseProduct> productResponse = ApiResponse.success(
                "Product fetched successfully",
                catalog.findById("E004").orElse(null)
        );
        System.out.println(productResponse);
        System.out.println("Data: " + productResponse.getData().getName());

        // T = List<BaseProduct>
        ApiResponse<List<BaseProduct>> listResponse = ApiResponse.success(
                "Products fetched",
                catalog.getAllProducts()
        );
        System.out.println("\n" + listResponse);
        System.out.println("Total products: " + listResponse.getData().size());

        // T = String
        ApiResponse<String> createdResponse = ApiResponse.created(
                "User registered successfully", "U003"
        );
        System.out.println("\n" + createdResponse);

        // Error responses
        ApiResponse<BaseProduct> notFound = ApiResponse.notFound("Product not found");
        System.out.println("\nNot found: " + notFound);

        ApiResponse<Void> unauthorized = ApiResponse.unauthorized();
        System.out.println("Unauthorized: " + unauthorized);

        ApiResponse<Void> validationError = ApiResponse.validationError(
                List.of("Name is required", "Price must be positive", "Stock cannot be negative")
        );
        System.out.println("Validation errors: " + validationError.getErrors());

        // ── Page<T> ───────────────────────────────────────
        System.out.println("\n--- Page<T> + PaginationUtil ---");

        // ✅ catalog is in scope here — declared at top of EC-005
        List<BaseProduct> allProducts = catalog.getAllProducts();
        System.out.println("Total products in catalog: " + allProducts.size());

        Page<BaseProduct> page0 = PaginationUtil.paginate(allProducts, 0, 2);
        System.out.println("\n" + page0);
        System.out.println("Page 0 products:");
        page0.getContent().forEach(prod -> System.out.println("  " + prod.getName()));
        System.out.println("isFirst: " + page0.isFirst() + " | hasNext: " + page0.hasNext());

        Page<BaseProduct> page1 = PaginationUtil.paginate(allProducts, 1, 2);
        System.out.println("\n" + page1);
        System.out.println("Page 1 products:");
        page1.getContent().forEach(prod -> System.out.println("  " + prod.getName()));
        System.out.println("hasPrevious: " + page1.hasPrevious() + " | hasNext: " + page1.hasNext());

        Page<BaseProduct> page2 = PaginationUtil.paginate(allProducts, 2, 2);
        System.out.println("\n" + page2);
        System.out.println("isLast: " + page2.isLast());

        // Nesting generics — ApiResponse wrapping a Page
        ApiResponse<Page<BaseProduct>> pagedResponse = ApiResponse.success(
                "Products fetched", page0
        );
        System.out.println("\nPaged API Response: " + pagedResponse);
        System.out.println("Page info: " + pagedResponse.getData());
        System.out.println("Items on page: " + pagedResponse.getData().getNumberOfElements());
    }
}