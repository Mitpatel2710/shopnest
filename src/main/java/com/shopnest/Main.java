package com.shopnest;

import com.shopnest.exception.*;
import com.shopnest.model.*;
import com.shopnest.service.*;
import com.shopnest.service.ProductAnalyticsService;
import com.shopnest.util.ApiResponse;
import com.shopnest.util.AppConfig;
import com.shopnest.util.Page;
import com.shopnest.util.PaginationUtil;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

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


        // ──────────────────────────────────────────────────
        // EC-007 — Streams & Lambdas
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-007: Streams & Lambdas =====");

        ProductAnalyticsService analytics =
                new ProductAnalyticsService(catalog.getAllProducts());

        // ── 1. Filtering ──────────────────────────────────
        System.out.println("\n--- Filtering ---");
        System.out.println("Electronics only:");
        analytics.getByCategory("Electronics")
                .forEach(prod -> System.out.println("  " + prod.getName()));

        System.out.println("\nAvailable under ₹15000 in Electronics:");
        analytics.getAvailableUnderPrice("Electronics", 15000)
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\nDiscount eligible:");
        analytics.getDiscountEligible()
                .forEach(prod -> System.out.println("  " + prod.getName()));

        // ── 2. Mapping ────────────────────────────────────
        System.out.println("\n--- Mapping ---");
        System.out.println("Product summaries:");
        analytics.toSummaries()
                .forEach(s -> System.out.println("  " + s.getDisplayLabel()));

        System.out.println("\nProduct names only:");
        System.out.println("  " + analytics.getProductNames());

        System.out.println("\nPrices after 15% discount:");
        analytics.getDiscountedPrices(15)
                .forEach(price -> System.out.printf("  ₹%.2f%n", price));

        // ── 3. Sorting ────────────────────────────────────
        System.out.println("\n--- Sorting ---");
        System.out.println("By price (low to high):");
        analytics.sortByPriceAsc()
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\nBy category then price:");
        analytics.sortByCategoryThenPrice()
                .forEach(prod -> System.out.println("  " + prod.getCategory() + " | " + prod.getName() + " ₹" + prod.getPrice()));

        // ── 4. Grouping ───────────────────────────────────
        System.out.println("\n--- Grouping ---");
        System.out.println("Count by category:");
        analytics.countByCategory()
                .forEach((cat, count) -> System.out.println("  " + cat + ": " + count));

        System.out.println("\nAverage price by category:");
        analytics.avgPriceByCategory()
                .forEach((cat, avg) -> System.out.printf("  %s: ₹%.2f%n", cat, avg));

        System.out.println("\nGrouped by price range:");
        analytics.groupByPriceRange()
                .forEach((range, prods) -> {
                    System.out.println("  " + range.getLabel() + ":");
                    prods.forEach(prod -> System.out.println("    " + prod.getName() + " ₹" + prod.getPrice()));
                });

        System.out.println("\nPartitioned by availability:");
        Map<Boolean, List<BaseProduct>> partitioned = analytics.partitionByAvailability();
        System.out.println("  Available: "    + partitioned.get(true).size());
        System.out.println("  Unavailable: "  + partitioned.get(false).size());

        System.out.println("\nMost expensive per category:");
        analytics.mostExpensiveByCategory()
                .forEach((cat, prod) -> prod.ifPresent(ep ->
                        System.out.println("  " + cat + ": " + ep.getName() + " ₹" + ep.getPrice())));

        // ── 5. Aggregating ────────────────────────────────
        System.out.println("\n--- Aggregating ---");
        System.out.printf("  Total inventory value: ₹%.2f%n", analytics.getTotalInventoryValue());
        analytics.getAveragePrice().ifPresent(avg -> System.out.printf("  Average price: ₹%.2f%n", avg));
        analytics.getMaxPrice().ifPresent(max ->     System.out.printf("  Max price: ₹%.2f%n", max));
        analytics.getMinPrice().ifPresent(min ->     System.out.printf("  Min price: ₹%.2f%n", min));
        System.out.println("  Available products: " + analytics.countAvailable());

        IntSummaryStatistics stockStats = analytics.getStockStatistics();
        System.out.println("  Stock stats — min: " + stockStats.getMin()
                + ", max: " + stockStats.getMax()
                + ", avg: " + stockStats.getAverage()
                + ", total: " + stockStats.getSum());

        // ── 6. Reducing ───────────────────────────────────
        System.out.println("\n--- Reducing ---");
        System.out.printf("  Total price (reduce): ₹%.2f%n", analytics.getTotalPriceWithReduce());
        analytics.getMostExpensive()
                .ifPresent(prod -> System.out.println("  Most expensive (reduce): " + prod.getName()));

        // ── 7. Collecting ─────────────────────────────────
        System.out.println("\n--- Collecting ---");
        System.out.println("  CSV: "       + analytics.getProductNamesCsv());
        System.out.println("  Formatted: " + analytics.getProductNamesFormatted());
        System.out.println("  Unique categories: " + analytics.getUniqueCategories());

        // ── 8. Advanced ───────────────────────────────────
        System.out.println("\n--- Advanced ---");
        System.out.println("Top 3 most expensive:");
        analytics.getTopN(3)
                .forEach(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));

        System.out.println("\nDistinct categories (sorted):");
        System.out.println("  " + analytics.getDistinctCategories());

        System.out.println("\nMatch checks:");
        System.out.println("  Any over ₹100000? " + analytics.anyMatch(prod -> prod.getPrice() > 100000));
        System.out.println("  All available? "     + analytics.allMatch(BaseProduct::isAvailable));
        System.out.println("  None free? "         + analytics.noneMatch(prod -> prod.getPrice() == 0));

        System.out.println("\nFind first Electronics under ₹15000:");
        analytics.findFirst(prod ->
                        prod.getCategory().equals("Electronics") && prod.getPrice() < 15000)
                .ifPresent(prod -> System.out.println("  " + prod.getName() + " ₹" + prod.getPrice()));


        // ──────────────────────────────────────────────────
        // EC-008 — Custom Exceptions
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-008: Custom Exceptions =====");

        // ── ProductNotFoundException ──────────────────────
        System.out.println("\n--- ProductNotFoundException ---");
        try {
            BaseProduct missing = catalog.findById("INVALID")
                    .orElseThrow(() -> new ProductNotFoundException("INVALID"));
        } catch (ProductNotFoundException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── OutOfStockException ───────────────────────────
        System.out.println("\n--- OutOfStockException ---");
        try {
            BaseProduct prod = catalog.findById("E004")
                    .orElseThrow(() -> new ProductNotFoundException("E004"));
            int requested = 9999;
            if (requested > prod.getStockQuantity()) {
                throw new OutOfStockException(prod.getId(), requested, prod.getStockQuantity());
            }
        } catch (OutOfStockException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── DuplicateProductException ─────────────────────
        System.out.println("\n--- DuplicateProductException ---");
        try {
            throw new DuplicateProductException("E004");
        } catch (DuplicateProductException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── InvalidProductException ───────────────────────
        System.out.println("\n--- InvalidProductException ---");
        try {
            throw new InvalidProductException("P_NEW",
                    List.of("Name is required", "Price must be positive", "Category cannot be empty"));
        } catch (InvalidProductException ex) {
            System.out.println("   Validation errors: " + ex.getValidationErrors());
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── InvalidOrderStateException ────────────────────
        System.out.println("\n--- InvalidOrderStateException ---");
        try {
            throw new InvalidOrderStateException("ORD001", OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        } catch (InvalidOrderStateException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Current: "   + ex.getCurrentStatus());
            System.out.println("   Attempted: " + ex.getAttemptedStatus());
            System.out.println("   Response: "  + response);
        }

        // ── EmptyCartException ────────────────────────────
        System.out.println("\n--- EmptyCartException ---");
        try {
            Cart emptyCart = new Cart("CART999", user);
            if (emptyCart.isEmpty()) {
                throw new EmptyCartException(user.getId());
            }
        } catch (EmptyCartException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── UserNotFoundException ─────────────────────────
        System.out.println("\n--- UserNotFoundException ---");
        try {
            throw new UserNotFoundException("email", "unknown@test.com");
        } catch (UserNotFoundException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── UnauthorizedAccessException ───────────────────
        System.out.println("\n--- UnauthorizedAccessException ---");
        try {
            if (!user.isAdmin()) {
                throw new UnauthorizedAccessException(user.getId(), "delete products");
            }
        } catch (UnauthorizedAccessException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Response: " + response);
        }

        // ── PaymentFailedException ────────────────────────
        System.out.println("\n--- PaymentFailedException ---");
        try {
            throw new PaymentFailedException("ORD001", 89999.0, "Insufficient balance");
        } catch (PaymentFailedException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Reason: "   + ex.getReason());
            System.out.println("   Amount: ₹"  + ex.getAmount());
            System.out.println("   Response: " + response);
        }

        // ── InvalidPaymentMethodException ────────────────
        System.out.println("\n--- InvalidPaymentMethodException ---");
        try {
            throw new InvalidPaymentMethodException("BITCOIN", "ORD001");
        } catch (InvalidPaymentMethodException ex) {
            ApiResponse<?> response = GlobalExceptionHandler.handle(ex);
            System.out.println("   Method: "   + ex.getMethod());
            System.out.println("   Response: " + response);
        }

        // ── Exception chaining ────────────────────────────
        System.out.println("\n--- Exception Chaining ---");
        try {
            try {
                // Simulate a low-level DB error
                throw new RuntimeException("DB connection timeout");
            } catch (RuntimeException dbEx) {
                // Wrap it in our domain exception — chaining the cause
                throw new ShopNestException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Failed to fetch product due to DB error",
                        500, dbEx
                );
            }
        } catch (ShopNestException ex) {
            System.out.println("   Exception : " + ex.getMessage());
            System.out.println("   Caused by : " + ex.getCause().getMessage());
            ApiResponse<?> response = GlobalExceptionHandler.handleUnexpected(ex);
            System.out.println("   Response  : " + response);
        }

        // ──────────────────────────────────────────────────
        // EC-009 — Design Patterns
        // ──────────────────────────────────────────────────
        System.out.println("\n===== EC-009: Design Patterns =====");

        // ── 1. Builder Pattern ────────────────────────────
        System.out.println("\n--- Builder Pattern ---");

        Product builtProduct = ProductBuilder.newProduct()
                .id("P002")
                .name("Samsung Galaxy S24")
                .price(74999.0)
                .stock(30)
                .category("Electronics")
                .description("Latest Samsung flagship")
                .imageUrl("https://shopnest.com/images/s24.jpg")
                .build();

        System.out.println("Built: " + builtProduct);

        // Builder with validation error
        System.out.println("\nBuilder validation test:");
        try {
            Product invalid = ProductBuilder.newProduct()
                    .id("")           // invalid
                    .price(-100)      // invalid
                    .build();
        } catch (InvalidProductException ex) {
            System.out.println("  Caught: " + ex.getValidationErrors());
        }

        // ── 2. Singleton Pattern ──────────────────────────
        System.out.println("\n--- Singleton Pattern ---");

        AppConfig config1 = AppConfig.getInstance();
        AppConfig config2 = AppConfig.getInstance();
        AppConfig config3 = AppConfig.getInstance();

        System.out.println("Config: " + config1);
        System.out.println("Same instance? " + (config1 == config2));    // true
        System.out.println("Same instance? " + (config2 == config3));    // true
        System.out.println("Max cart items: " + config1.getMaxCartItems());
        System.out.println("Max discount: "   + config1.getMaxDiscountPercent() + "%");

        // ── 3. Factory Pattern ────────────────────────────
        System.out.println("\n--- Factory Pattern ---");

        // Caller never uses 'new' directly — Factory decides
        PaymentProcessor upiProcessor  = PaymentProcessorFactory.getProcessor(PaymentMethod.UPI,  "ORD001");
        PaymentProcessor cardProcessor = PaymentProcessorFactory.getProcessor(PaymentMethod.CARD, "ORD001");
        PaymentProcessor codProcessor  = PaymentProcessorFactory.getProcessor(PaymentMethod.COD,  "ORD001");

        // Reuse order from EC-004
        String upiTxn  = upiProcessor.processPayment(order, 89999.0);
        System.out.println("  UPI TXN ID: " + upiTxn);

        String cardTxn = cardProcessor.processPayment(order, 89999.0);
        System.out.println("  CARD TXN ID: " + cardTxn);

        String codTxn  = codProcessor.processPayment(order, 15000.0);
        System.out.println("  COD TXN ID: " + codTxn);

        // COD limit validation
        System.out.println("  COD valid for ₹15000? " + codProcessor.validate(15000));
        System.out.println("  COD valid for ₹75000? " + codProcessor.validate(75000));

        // Get by string — useful for API requests
        PaymentProcessor fromString = PaymentProcessorFactory.getProcessor("upi", "ORD001");
        System.out.println("  From string: " + fromString.getPaymentMethodName());

        // Invalid payment method
        try {
            PaymentProcessorFactory.getProcessor("CRYPTO", "ORD001");
        } catch (InvalidPaymentMethodException ex) {
            System.out.println("  Expected error: " + ex.getMessage());
        }

        // ── 4. Strategy Pattern ───────────────────────────
        System.out.println("\n--- Strategy Pattern ---");

        BaseProduct strategyProduct = catalog.findById("E006")
                .orElseThrow(() -> new ProductNotFoundException("E006"));

        DiscountService discountService = new DiscountService(new NoDiscountStrategy());

        // No discount
        System.out.println("\nNo Discount:");
        discountService.getFinalPrice(strategyProduct, user);

        // Swap to seasonal — runtime strategy change
        System.out.println("\nDiwali Sale:");
        discountService.setStrategy(new SeasonalDiscountStrategy("Diwali", 25.0));
        discountService.getFinalPrice(strategyProduct, user);

        // Swap to loyalty
        System.out.println("\nLoyalty Discount (CUSTOMER):");
        discountService.setStrategy(new LoyaltyDiscountStrategy());
        discountService.getFinalPrice(strategyProduct, user);

        // Loyalty for seller
        user.promoteToSeller();
        System.out.println("\nLoyalty Discount (SELLER):");
        discountService.getFinalPrice(strategyProduct, user);

        // Coupon
        System.out.println("\nCoupon FESTIVE30:");
        CouponDiscountStrategy coupon = new CouponDiscountStrategy("FESTIVE30");
        System.out.println("  Valid coupon? " + coupon.isValidCoupon());
        discountService.setStrategy(coupon);
        discountService.getFinalPrice(strategyProduct, user);

        // Invalid coupon
        System.out.println("\nInvalid Coupon FAKE99:");
        CouponDiscountStrategy fakeCoupon = new CouponDiscountStrategy("FAKE99");
        System.out.println("  Valid coupon? " + fakeCoupon.isValidCoupon());
        discountService.setStrategy(fakeCoupon);
        discountService.getFinalPrice(strategyProduct, user);

        // ── 5. Observer Pattern ───────────────────────────
        System.out.println("\n--- Observer Pattern ---");

        OrderEventPublisher publisher = new OrderEventPublisher();

        // Register all listeners
        publisher.subscribe(new EmailNotificationListener());
        publisher.subscribe(new InventoryUpdateListener());
        publisher.subscribe(new SellerNotificationListener());

        System.out.println("\nListeners registered: " + publisher.getListenerCount());

        // Publish events — all listeners notified automatically
        publisher.publish(new OrderEvent(OrderEvent.Type.ORDER_PLACED,    order));
        publisher.publish(new OrderEvent(OrderEvent.Type.ORDER_CONFIRMED, order));
        publisher.publish(new OrderEvent(OrderEvent.Type.ORDER_SHIPPED,   order));
        publisher.publish(new OrderEvent(OrderEvent.Type.ORDER_DELIVERED, order));

        // Unsubscribe seller listener
        System.out.println();
        publisher.unsubscribe(publisher.getListeners().get(2));
        System.out.println("After unsubscribe — listeners: " + publisher.getListenerCount());

        publisher.publish(new OrderEvent(OrderEvent.Type.ORDER_CANCELLED, order));



    }



}