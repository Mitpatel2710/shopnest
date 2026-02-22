package com.shopnest.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product — Unit Tests")
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        // Fresh product before each test — tests are independent
        product = new Product(
                "P001", "iPhone 15", "Latest Apple phone",
                79999.0, 50, "Electronics"
        );
    }

    // ── Constructor & Validation ───────────────────────────
    @Nested
    @DisplayName("Constructor Validation")
    class ConstructorValidation {

        @Test
        @DisplayName("Should create product with valid fields")
        void shouldCreateProductWithValidFields() {
            assertNotNull(product);
            assertEquals("P001",        product.getId());
            assertEquals("iPhone 15",   product.getName());
            assertEquals(79999.0,        product.getPrice());
            assertEquals(50,             product.getStockQuantity());
            assertEquals("Electronics", product.getCategory());
            assertTrue(product.isActive());
            assertNotNull(product.getCreatedAt());
        }

        @Test
        @DisplayName("Should throw when ID is null")
        void shouldThrowWhenIdIsNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Product(null, "iPhone", "desc", 999.0, 10, "Electronics")
            );
            assertTrue(ex.getMessage().contains("ID"));
        }

        @Test
        @DisplayName("Should throw when ID is blank")
        void shouldThrowWhenIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Product("  ", "iPhone", "desc", 999.0, 10, "Electronics"));
        }

        @Test
        @DisplayName("Should throw when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Product("P001", null, "desc", 999.0, 10, "Electronics"));
        }

        @Test
        @DisplayName("Should throw when price is negative")
        void shouldThrowWhenPriceIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Product("P001", "iPhone", "desc", -1.0, 10, "Electronics"));
        }

        @Test
        @DisplayName("Should allow price of zero")
        void shouldAllowZeroPrice() {
            assertDoesNotThrow(
                    () -> new Product("P001", "Free Item", "desc", 0.0, 10, "Electronics"));
        }

        @Test
        @DisplayName("Should throw when stock is negative")
        void shouldThrowWhenStockIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Product("P001", "iPhone", "desc", 999.0, -1, "Electronics"));
        }
    }

    // ── isAvailable ───────────────────────────────────────
    @Nested
    @DisplayName("Availability")
    class Availability {

        @Test
        @DisplayName("Should be available when active and stock > 0")
        void shouldBeAvailableWhenActiveAndInStock() {
            assertTrue(product.isAvailable());
        }

        @Test
        @DisplayName("Should not be available when inactive")
        void shouldNotBeAvailableWhenInactive() {
            product.setActive(false);
            assertFalse(product.isAvailable());
        }

        @Test
        @DisplayName("Should not be available when stock is zero")
        void shouldNotBeAvailableWhenStockIsZero() {
            product.reduceStock(50); // reduce all stock
            assertFalse(product.isAvailable());
        }
    }

    // ── reduceStock ───────────────────────────────────────
    @Nested
    @DisplayName("Stock Management")
    class StockManagement {

        @Test
        @DisplayName("Should reduce stock by given quantity")
        void shouldReduceStock() {
            product.reduceStock(10);
            assertEquals(40, product.getStockQuantity());
        }

        @Test
        @DisplayName("Should reduce stock to zero")
        void shouldReduceStockToZero() {
            product.reduceStock(50);
            assertEquals(0, product.getStockQuantity());
        }

        @Test
        @DisplayName("Should throw when reducing more than available")
        void shouldThrowWhenReducingMoreThanAvailable() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> product.reduceStock(51)
            );
            assertTrue(ex.getMessage().contains("50")); // available qty in message
        }

        @Test
        @DisplayName("Should throw when reduce quantity is zero")
        void shouldThrowWhenReduceQuantityIsZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> product.reduceStock(0));
        }

        @Test
        @DisplayName("Should throw when reduce quantity is negative")
        void shouldThrowWhenReduceQuantityIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> product.reduceStock(-5));
        }

        @Test
        @DisplayName("Should add stock correctly")
        void shouldAddStock() {
            product.addStock(20);
            assertEquals(70, product.getStockQuantity());
        }

        @Test
        @DisplayName("Should throw when adding zero stock")
        void shouldThrowWhenAddingZeroStock() {
            assertThrows(IllegalArgumentException.class,
                    () -> product.addStock(0));
        }
    }

    // ── Discount ──────────────────────────────────────────
    @Nested
    @DisplayName("Discount Calculation")
    class DiscountCalculation {

        @ParameterizedTest(name = "{0}% discount on ₹79999 should give ₹{1}")
        @CsvSource({
                "0,   79999.0",
                "10,  71999.1",
                "50,  39999.5",
                "100, 0.0"
        })
        void shouldCalculateDiscountCorrectly(double percent, double expected) {
            assertEquals(expected, product.getPriceAfterDiscount(percent), 0.01);
        }

        @ParameterizedTest(name = "Discount of {0}% should throw")
        @ValueSource(doubles = {-1.0, 101.0, 150.0})
        void shouldThrowForInvalidDiscount(double percent) {
            assertThrows(IllegalArgumentException.class,
                    () -> product.getPriceAfterDiscount(percent));
        }
    }

    // ── equals & hashCode ─────────────────────────────────
    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("Two products with same ID should be equal")
        void shouldBeEqualWithSameId() {
            Product other = new Product("P001", "Different Name", "", 1.0, 1, "Cat");
            assertEquals(product, other);
        }

        @Test
        @DisplayName("Two products with different ID should not be equal")
        void shouldNotBeEqualWithDifferentId() {
            Product other = new Product("P002", "iPhone 15", "", 79999.0, 50, "Electronics");
            assertNotEquals(product, other);
        }

        @Test
        @DisplayName("Same ID should produce same hashCode")
        void shouldHaveSameHashCodeForSameId() {
            Product other = new Product("P001", "Different", "", 1.0, 1, "Cat");
            assertEquals(product.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("Product should equal itself")
        void shouldEqualItself() {
            assertEquals(product, product);
        }

        @Test
        @DisplayName("Product should not equal null")
        void shouldNotEqualNull() {
            assertNotEquals(product, null);
        }
    }

    // ── toString ──────────────────────────────────────────
    @Test
    @DisplayName("toString should contain key fields")
    void toStringShouldContainKeyFields() {
        String str = product.toString();
        assertTrue(str.contains("P001"));
        assertTrue(str.contains("iPhone 15"));
        assertTrue(str.contains("79999"));
    }

    @AfterEach
    void tearDown() {
        product = null; // help GC
    }
}