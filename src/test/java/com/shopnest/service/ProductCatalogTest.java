package com.shopnest.service;

import com.shopnest.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductCatalog — Unit Tests")
class ProductCatalogTest {

    private ProductCatalog catalog;
    private ElectronicsProduct laptop;
    private ClothingProduct tshirt;

    @BeforeEach
    void setUp() {
        catalog = new ProductCatalog();
        laptop  = new ElectronicsProduct("E001", "MacBook", "Laptop",   90000.0, 10, "Apple", 12);
        tshirt  = new ClothingProduct("C001",   "Nike Tee", "T-Shirt",  1500.0,  50, "Nike",  ClothingProduct.Size.L, "Black");
        catalog.addProduct(laptop);
        catalog.addProduct(tshirt);
    }

    @Nested
    @DisplayName("Add and Remove")
    class AddAndRemove {

        @Test
        @DisplayName("Should add products correctly")
        void shouldAddProducts() {
            assertEquals(2, catalog.getTotalProductCount());
        }

        @Test
        @DisplayName("Should throw for duplicate product ID")
        void shouldThrowForDuplicateId() {
            assertThrows(IllegalArgumentException.class,
                    () -> catalog.addProduct(laptop));
        }

        @Test
        @DisplayName("Should remove product correctly")
        void shouldRemoveProduct() {
            boolean removed = catalog.removeProduct("E001");
            assertTrue(removed);
            assertEquals(1, catalog.getTotalProductCount());
        }

        @Test
        @DisplayName("Should return false when removing non-existent product")
        void shouldReturnFalseForNonExistentRemoval() {
            assertFalse(catalog.removeProduct("INVALID"));
        }
    }

    @Nested
    @DisplayName("Find and Search")
    class FindAndSearch {

        @Test
        @DisplayName("Should find product by ID")
        void shouldFindById() {
            assertTrue(catalog.findById("E001").isPresent());
            assertEquals("MacBook", catalog.findById("E001").get().getName());
        }

        @Test
        @DisplayName("Should return empty Optional for unknown ID")
        void shouldReturnEmptyForUnknownId() {
            assertFalse(catalog.findById("UNKNOWN").isPresent());
        }

        @Test
        @DisplayName("Should find products by category")
        void shouldFindByCategory() {
            assertEquals(1, catalog.findByCategory("Electronics").size());
            assertEquals(1, catalog.findByCategory("Clothing").size());
        }

        @Test
        @DisplayName("Should return empty list for unknown category")
        void shouldReturnEmptyListForUnknownCategory() {
            assertTrue(catalog.findByCategory("Unknown").isEmpty());
        }

        @Test
        @DisplayName("Should search by keyword in name")
        void shouldSearchByKeyword() {
            assertEquals(1, catalog.search("MacBook").size());
            assertEquals(1, catalog.search("nike").size());  // case insensitive
            assertEquals(0, catalog.search("INVALID").size());
        }

        @Test
        @DisplayName("Should filter by price range")
        void shouldFilterByPriceRange() {
            assertEquals(1, catalog.findByPriceRange(0, 5000).size());
            assertEquals(1, catalog.findByPriceRange(50000, 100000).size());
            assertEquals(2, catalog.findByPriceRange(0, 100000).size());
        }
    }

    @Nested
    @DisplayName("Featured Products")
    class FeaturedProducts {

        @Test
        @DisplayName("Should mark and check featured products")
        void shouldMarkAsFeatured() {
            catalog.markAsFeatured("E001");
            assertTrue(catalog.isFeatured("E001"));
            assertFalse(catalog.isFeatured("C001"));
            assertEquals(1, catalog.getFeaturedProducts().size());
        }

        @Test
        @DisplayName("Should not duplicate featured products")
        void shouldNotDuplicateFeatured() {
            catalog.markAsFeatured("E001");
            catalog.markAsFeatured("E001"); // duplicate
            assertEquals(1, catalog.getFeaturedProducts().size());
        }

        @Test
        @DisplayName("Should unmark featured product")
        void shouldUnmarkFeatured() {
            catalog.markAsFeatured("E001");
            catalog.unmarkFeatured("E001");
            assertFalse(catalog.isFeatured("E001"));
        }
    }
}