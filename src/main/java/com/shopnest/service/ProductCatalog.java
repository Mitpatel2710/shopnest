package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.Order;

import java.util.*;
import java.util.stream.Collectors;

public class ProductCatalog {

    // ── Why each collection was chosen ────────────────────
    //
    // ArrayList   — ordered, index-based access, best for iteration
    // HashMap     — O(1) lookup by key, unordered
    // LinkedHashMap — O(1) lookup + maintains insertion order
    // HashSet     — O(1) contains check, no duplicates
    // LinkedList as Queue — FIFO order processing
    // TreeMap     — sorted by key (category name A-Z)

    private final List<BaseProduct> allProducts;                    // master list
    private final Map<String, BaseProduct> productById;             // fast ID lookup
    private final Map<String, List<BaseProduct>> byCategory;        // category buckets
    private final Set<String> featuredProductIds;                   // no duplicates
    private final Queue<Order> orderQueue;                          // FIFO processing
    private final Map<String, Integer> categoryProductCount;        // sorted category stats

    public ProductCatalog(){
        this.allProducts            = new ArrayList<>();
        this.productById            = new HashMap<>();
        this.byCategory             = new LinkedHashMap<>();    // preserves category insertion order
        this.featuredProductIds     = new HashSet<>();
        this.orderQueue             = new LinkedList<>();
        this.categoryProductCount   = new TreeMap<>();          // categories sorted A-Z
    }

    // Add Product
    public void addProduct(BaseProduct product){
        if(product == null) throw new IllegalArgumentException("Product cannot be null");
        if(productById.containsKey(product.getId()))
            throw new IllegalArgumentException("Product already exists: "+ product.getId());

        // add to master list
        allProducts.add(product);

        // add to id map for 0(1) lookup
        productById.put(product.getId(),product);

        // add to category bucket -  create bucket if first product in category
        byCategory.computeIfAbsent(product.getCategory(),k -> new ArrayList<>())
                .add(product);

        // update category count
        categoryProductCount.merge(product.getCategory(),1,Integer::sum);
    }

    // ── Remove Product ────────────────────────────────────
    public boolean removeProduct(String productId) {
        BaseProduct product = productById.remove(productId);
        if (product == null) return false;

        allProducts.remove(product);

        List<BaseProduct> categoryList = byCategory.get(product.getCategory());
        if (categoryList != null) {
            categoryList.remove(product);
            if (categoryList.isEmpty()) byCategory.remove(product.getCategory());
        }

        featuredProductIds.remove(productId);
        categoryProductCount.merge(product.getCategory(), -1, Integer::sum);
        return true;
    }

    // ── Fetch by ID ───────────────────────────────────────
    public Optional<BaseProduct> findById(String productId) {
        return Optional.ofNullable(productById.get(productId));
    }

    // ── Fetch by Category ─────────────────────────────────
    public List<BaseProduct> findByCategory(String category) {
        return byCategory.getOrDefault(category, Collections.emptyList());
    }

    // ── Search by name (case-insensitive) ─────────────────
    public List<BaseProduct> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllProducts();
        String lower = keyword.toLowerCase();
        return allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(lower)
                        || p.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // ── Filter by price range ─────────────────────────────
    public List<BaseProduct> findByPriceRange(double min, double max) {
        if (min < 0 || max < min) throw new IllegalArgumentException("Invalid price range");
        return allProducts.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
    }

    // ── Sort products ─────────────────────────────────────
    public List<BaseProduct> getSortedByPrice(boolean ascending) {
        return allProducts.stream()
                .sorted(ascending
                        ? Comparator.comparingDouble(BaseProduct::getPrice)
                        : Comparator.comparingDouble(BaseProduct::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public List<BaseProduct> getSortedByName() {
        return allProducts.stream()
                .sorted(Comparator.comparing(BaseProduct::getName))
                .collect(Collectors.toList());
    }

    // ── Available products only ───────────────────────────
    public List<BaseProduct> getAvailableProducts() {
        return allProducts.stream()
                .filter(BaseProduct::isAvailable)
                .collect(Collectors.toList());
    }

    // ── Featured products ─────────────────────────────────
    public void markAsFeatured(String productId) {
        if (!productById.containsKey(productId))
            throw new IllegalArgumentException("Product not found: " + productId);
        featuredProductIds.add(productId);  // Set ignores duplicates silently
    }

    public void unmarkFeatured(String productId) {
        featuredProductIds.remove(productId);
    }

    public List<BaseProduct> getFeaturedProducts() {
        return featuredProductIds.stream()
                .map(productById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean isFeatured(String productId) {
        return featuredProductIds.contains(productId); // O(1) — HashSet strength
    }

    // ── Order Queue ───────────────────────────────────────
    public void enqueueOrder(Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        orderQueue.offer(order);    // offer = add to tail of queue
    }

    public Optional<Order> processNextOrder() {
        return Optional.ofNullable(orderQueue.poll()); // poll = remove from head, null if empty
    }

    public Optional<Order> peekNextOrder() {
        return Optional.ofNullable(orderQueue.peek()); // peek = look at head without removing
    }

    public int getOrderQueueSize() {
        return orderQueue.size();
    }

    // ── Statistics ────────────────────────────────────────
    public Map<String, Integer> getCategoryProductCount() {
        return Collections.unmodifiableMap(categoryProductCount); // TreeMap — sorted A-Z
    }

    public Map<String, Double> getAveragePriceByCategory() {
        return allProducts.stream()
                .collect(Collectors.groupingBy(
                        BaseProduct::getCategory,
                        Collectors.averagingDouble(BaseProduct::getPrice)
                ));
    }

    public Optional<BaseProduct> getMostExpensive() {
        return allProducts.stream()
                .max(Comparator.comparingDouble(BaseProduct::getPrice));
    }

    public Optional<BaseProduct> getCheapest() {
        return allProducts.stream()
                .min(Comparator.comparingDouble(BaseProduct::getPrice));
    }

    // ── Getters ───────────────────────────────────────────
    public List<BaseProduct> getAllProducts() {
        return Collections.unmodifiableList(allProducts);
    }

    public Set<String> getAllCategories() {
        return Collections.unmodifiableSet(byCategory.keySet());
    }

    public int getTotalProductCount() {
        return allProducts.size();
    }

    @Override
    public String toString() {
        return "ProductCatalog{products=" + allProducts.size() +
                ", categories=" + byCategory.size() +
                ", featured=" + featuredProductIds.size() +
                ", ordersInQueue=" + orderQueue.size() + "}";
    }


}
