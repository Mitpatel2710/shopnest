package com.shopnest.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart — Unit Tests")
class CartTest {

    private Cart cart;
    private User user;
    private ElectronicsProduct laptop;
    private ClothingProduct tshirt;

    @BeforeEach
    void setUp() {
        user   = new User("U001", "Rahul", "Sharma", "rahul@test.com", "pass123");
        laptop = new ElectronicsProduct("E001", "MacBook", "Laptop", 90000.0, 10, "Apple", 12);
        tshirt = new ClothingProduct("C001", "Nike Tee", "Tshirt", 1500.0, 50, "Nike", ClothingProduct.Size.L, "Black");
        cart   = new Cart("CART001", user);
    }

    @Nested
    @DisplayName("Add Items")
    class AddItems {

        @Test
        @DisplayName("Should add a product to cart")
        void shouldAddProduct() {
            cart.addItem(laptop, 1);
            assertEquals(1, cart.getItems().size());
            assertEquals(1, cart.getTotalItems());
        }

        @Test
        @DisplayName("Should increase quantity when same product added again")
        void shouldIncreaseQuantityForDuplicateProduct() {
            cart.addItem(tshirt, 2);
            cart.addItem(tshirt, 3);
            assertEquals(1,    cart.getItems().size()); // still one item
            assertEquals(5,    cart.getTotalItems());   // quantity = 5
        }

        @Test
        @DisplayName("Should add multiple different products")
        void shouldAddMultipleProducts() {
            cart.addItem(laptop, 1);
            cart.addItem(tshirt, 2);
            assertEquals(2, cart.getItems().size());
        }

        @Test
        @DisplayName("Should throw when adding null product")
        void shouldThrowForNullProduct() {
            assertThrows(IllegalArgumentException.class,
                    () -> cart.addItem(null, 1));
        }
    }

    @Nested
    @DisplayName("Remove Items")
    class RemoveItems {

        @Test
        @DisplayName("Should remove product from cart")
        void shouldRemoveProduct() {
            cart.addItem(laptop, 1);
            cart.addItem(tshirt, 2);
            cart.removeItem("E001");
            assertEquals(1, cart.getItems().size());
        }

        @Test
        @DisplayName("Should throw when removing product not in cart")
        void shouldThrowWhenRemovingNonExistentProduct() {
            assertThrows(IllegalArgumentException.class,
                    () -> cart.removeItem("INVALID"));
        }
    }

    @Nested
    @DisplayName("Total Calculation")
    class TotalCalculation {

        @Test
        @DisplayName("Should calculate total correctly")
        void shouldCalculateTotalCorrectly() {
            cart.addItem(laptop, 1);  // 90000
            cart.addItem(tshirt, 2); // 3000
            assertEquals(93000.0, cart.getTotalPrice(), 0.01);
        }

        @Test
        @DisplayName("Total should be zero for empty cart")
        void totalShouldBeZeroForEmptyCart() {
            assertEquals(0.0, cart.getTotalPrice(), 0.01);
        }
    }

    @Nested
    @DisplayName("Cart State")
    class CartState {

        @Test
        @DisplayName("New cart should be empty")
        void newCartShouldBeEmpty() {
            assertTrue(cart.isEmpty());
            assertEquals(0, cart.getTotalItems());
        }

        @Test
        @DisplayName("Should clear all items")
        void shouldClearAllItems() {
            cart.addItem(laptop, 1);
            cart.addItem(tshirt, 2);
            cart.clear();
            assertTrue(cart.isEmpty());
        }

        @Test
        @DisplayName("Should return unmodifiable list")
        void shouldReturnUnmodifiableList() {
            cart.addItem(laptop, 1);
            assertThrows(UnsupportedOperationException.class,
                    () -> cart.getItems().add(new CartItem(tshirt, 1)));
        }
    }
}