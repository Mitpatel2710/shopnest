package com.shopnest.service;

import com.shopnest.model.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ProductAnalyticsService {

    private final List<BaseProduct> products;

    public ProductAnalyticsService(List<BaseProduct> products) {
        if (products == null) throw new IllegalArgumentException("Products cannot be null");
        this.products = new ArrayList<>(products);
    }

    // ══════════════════════════════════════════════════
    // 1. FILTERING — filter() + Predicate<T>
    // ══════════════════════════════════════════════════

    // Filter by single category
    public List<BaseProduct> getByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // Filter available products only
    public List<BaseProduct> getAvailable() {
        return products.stream()
                .filter(BaseProduct::isAvailable)   // method reference
                .collect(Collectors.toList());
    }

    // Filter by max price
    public List<BaseProduct> getUnderPrice(double maxPrice) {
        return products.stream()
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Combining multiple predicates
    public List<BaseProduct> getAvailableUnderPrice(String category, double maxPrice) {
        Predicate<BaseProduct> inCategory  = p -> p.getCategory().equalsIgnoreCase(category);
        Predicate<BaseProduct> underPrice  = p -> p.getPrice() <= maxPrice;
        Predicate<BaseProduct> isAvailable = BaseProduct::isAvailable;

        return products.stream()
                .filter(inCategory.and(underPrice).and(isAvailable))
                .collect(Collectors.toList());
    }

    // Eligible for discount
    public List<BaseProduct> getDiscountEligible() {
        return products.stream()
                .filter(Discountable::isEligibleForDiscount)
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════════
    // 2. MAPPING — map() + Function<T, R>
    // ══════════════════════════════════════════════════

    // Product → ProductSummary (transform type)
    public List<ProductSummary> toSummaries() {
        return products.stream()
                .map(p -> new ProductSummary(
                        p.getId(), p.getName(),
                        p.getPrice(), p.getCategory(),
                        p.isAvailable()))
                .collect(Collectors.toList());
    }

    // Product → name only
    public List<String> getProductNames() {
        return products.stream()
                .map(BaseProduct::getName)          // method reference
                .collect(Collectors.toList());
    }

    // Product → price only
    public List<Double> getPrices() {
        return products.stream()
                .map(BaseProduct::getPrice)
                .collect(Collectors.toList());
    }

    // Product → discounted price (apply 10% discount)
    public List<Double> getDiscountedPrices(double discountPercent) {
        return products.stream()
                .map(p -> p.getPriceAfterDiscount(discountPercent))
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════════
    // 3. SORTING — sorted() + Comparator
    // ══════════════════════════════════════════════════

    // Sort by price ascending
    public List<BaseProduct> sortByPriceAsc() {
        return products.stream()
                .sorted(Comparator.comparingDouble(BaseProduct::getPrice))
                .collect(Collectors.toList());
    }

    // Sort by price descending
    public List<BaseProduct> sortByPriceDesc() {
        return products.stream()
                .sorted(Comparator.comparingDouble(BaseProduct::getPrice).reversed())
                .collect(Collectors.toList());
    }

    // Multi-field sort — category A-Z, then price low-high within category
    public List<BaseProduct> sortByCategoryThenPrice() {
        return products.stream()
                .sorted(Comparator.comparing(BaseProduct::getCategory)
                        .thenComparingDouble(BaseProduct::getPrice))
                .collect(Collectors.toList());
    }

    // Sort by name alphabetically
    public List<BaseProduct> sortByName() {
        return products.stream()
                .sorted(Comparator.comparing(BaseProduct::getName,
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════════
    // 4. GROUPING — groupingBy()
    // ══════════════════════════════════════════════════

    // Group by category
    public Map<String, List<BaseProduct>> groupByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(BaseProduct::getCategory));
    }

    // Group by price range using PriceRange enum
    public Map<PriceRange, List<BaseProduct>> groupByPriceRange() {
        return products.stream()
                .collect(Collectors.groupingBy(p -> PriceRange.of(p.getPrice())));
    }

    // Group by availability — true/false buckets
    public Map<Boolean, List<BaseProduct>> partitionByAvailability() {
        return products.stream()
                .collect(Collectors.partitioningBy(BaseProduct::isAvailable));
    }

    // Count per category
    public Map<String, Long> countByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        BaseProduct::getCategory,
                        Collectors.counting()
                ));
    }

    // Average price per category
    public Map<String, Double> avgPriceByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        BaseProduct::getCategory,
                        Collectors.averagingDouble(BaseProduct::getPrice)
                ));
    }

    // Most expensive product per category
    public Map<String, Optional<BaseProduct>> mostExpensiveByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        BaseProduct::getCategory,
                        Collectors.maxBy(Comparator.comparingDouble(BaseProduct::getPrice))
                ));
    }

    // ══════════════════════════════════════════════════
    // 5. AGGREGATING — sum, avg, min, max, count
    // ══════════════════════════════════════════════════

    public double getTotalInventoryValue() {
        return products.stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
    }

    public OptionalDouble getAveragePrice() {
        return products.stream()
                .mapToDouble(BaseProduct::getPrice)
                .average();
    }

    public OptionalDouble getMaxPrice() {
        return products.stream()
                .mapToDouble(BaseProduct::getPrice)
                .max();
    }

    public OptionalDouble getMinPrice() {
        return products.stream()
                .mapToDouble(BaseProduct::getPrice)
                .min();
    }

    public long countAvailable() {
        return products.stream()
                .filter(BaseProduct::isAvailable)
                .count();
    }

    public IntSummaryStatistics getStockStatistics() {
        return products.stream()
                .mapToInt(BaseProduct::getStockQuantity)
                .summaryStatistics();
    }

    // ══════════════════════════════════════════════════
    // 6. REDUCING — reduce()
    // ══════════════════════════════════════════════════

    // Total price of all products using reduce
    public double getTotalPriceWithReduce() {
        return products.stream()
                .map(BaseProduct::getPrice)
                .reduce(0.0, Double::sum);  // identity + accumulator
    }

    // Most expensive product using reduce
    public Optional<BaseProduct> getMostExpensive() {
        return products.stream()
                .reduce((a, b) -> a.getPrice() > b.getPrice() ? a : b);
    }

    // ══════════════════════════════════════════════════
    // 7. COLLECTING — toMap, toSet, joining
    // ══════════════════════════════════════════════════

    // Collect to Map: id → product
    public Map<String, BaseProduct> toProductMap() {
        return products.stream()
                .collect(Collectors.toMap(
                        BaseProduct::getId,     // key
                        Function.identity()     // value = the product itself
                ));
    }

    // Collect names to a Set (unique)
    public Set<String> getUniqueCategories() {
        return products.stream()
                .map(BaseProduct::getCategory)
                .collect(Collectors.toSet());
    }

    // Join product names into a single string
    public String getProductNamesCsv() {
        return products.stream()
                .map(BaseProduct::getName)
                .collect(Collectors.joining(", "));
    }

    // Join with prefix and suffix
    public String getProductNamesFormatted() {
        return products.stream()
                .map(BaseProduct::getName)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    // ══════════════════════════════════════════════════
    // 8. ADVANCED — flatMap, distinct, limit, skip
    // ══════════════════════════════════════════════════

    // Top N most expensive products
    public List<BaseProduct> getTopN(int n) {
        return products.stream()
                .sorted(Comparator.comparingDouble(BaseProduct::getPrice).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // Skip first N, get rest — useful for pagination
    public List<BaseProduct> skipFirst(int n) {
        return products.stream()
                .skip(n)
                .collect(Collectors.toList());
    }

    // Distinct categories (using map + distinct)
    public List<String> getDistinctCategories() {
        return products.stream()
                .map(BaseProduct::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Check if ANY product matches a condition
    public boolean anyMatch(Predicate<BaseProduct> condition) {
        return products.stream().anyMatch(condition);
    }

    // Check if ALL products match a condition
    public boolean allMatch(Predicate<BaseProduct> condition) {
        return products.stream().allMatch(condition);
    }

    // Check if NO product matches a condition
    public boolean noneMatch(Predicate<BaseProduct> condition) {
        return products.stream().noneMatch(condition);
    }

    // Find first product matching condition
    public Optional<BaseProduct> findFirst(Predicate<BaseProduct> condition) {
        return products.stream()
                .filter(condition)
                .findFirst();
    }
}