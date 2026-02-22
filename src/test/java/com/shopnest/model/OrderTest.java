package com.shopnest.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order — Unit Tests")
class OrderTest {

    private User user;
    private Cart cart;
    private Order order;
    private ElectronicsProduct laptop;

    @BeforeEach
    void setUp() {
        user   = new User("U001", "Rahul", "Sharma", "rahul@test.com", "pass123");
        laptop = new ElectronicsProduct("E001", "MacBook", "Laptop", 90000.0, 10, "Apple", 12);
        cart   = new Cart("CART001", user);
        cart.addItem(laptop, 1);
        order  = new Order("ORD001", user, cart, "123, MG Road, Bengaluru");
    }

    @Nested
    @DisplayName("Order Creation")
    class OrderCreation {

        @Test
        @DisplayName("Should create order from cart")
        void shouldCreateOrderFromCart() {
            assertNotNull(order);
            assertEquals("ORD001",           order.getId());
            assertEquals(OrderStatus.PENDING, order.getStatus());
            assertEquals(1,                  order.getOrderItems().size());
            assertEquals(90000.0,            order.getTotalAmount(), 0.01);
        }

        @Test
        @DisplayName("Should reduce product stock on order creation")
        void shouldReduceStockOnOrderCreation() {
            // laptop had 10 stock, ordered 1
            assertEquals(9, laptop.getStockQuantity());
        }

        @Test
        @DisplayName("Should throw for empty cart")
        void shouldThrowForEmptyCart() {
            Cart emptyCart = new Cart("CART002", user);
            assertThrows(IllegalArgumentException.class,
                    () -> new Order("ORD002", user, emptyCart, "Address"));
        }

        @Test
        @DisplayName("Should throw for null delivery address")
        void shouldThrowForNullAddress() {
            Cart freshCart = new Cart("CART003", user);
            ElectronicsProduct prod = new ElectronicsProduct(
                    "E002", "iPad", "Tablet", 50000.0, 5, "Apple", 12);
            freshCart.addItem(prod, 1);
            assertThrows(IllegalArgumentException.class,
                    () -> new Order("ORD003", user, freshCart, null));
        }
    }

    @Nested
    @DisplayName("Order State Machine")
    class OrderStateMachine {

        @Test
        @DisplayName("Full happy path: PENDING → CONFIRMED → SHIPPED → DELIVERED")
        void shouldFollowHappyPath() {
            assertEquals(OrderStatus.PENDING, order.getStatus());
            order.confirm();
            assertEquals(OrderStatus.CONFIRMED, order.getStatus());
            order.ship();
            assertEquals(OrderStatus.SHIPPED, order.getStatus());
            order.deliver();
            assertEquals(OrderStatus.DELIVERED, order.getStatus());
        }

        @Test
        @DisplayName("Should cancel PENDING order")
        void shouldCancelPendingOrder() {
            assertTrue(order.isCancellable());
            order.cancel();
            assertEquals(OrderStatus.CANCELLED, order.getStatus());
        }

        @Test
        @DisplayName("Should cancel CONFIRMED order")
        void shouldCancelConfirmedOrder() {
            order.confirm();
            assertTrue(order.isCancellable());
            order.cancel();
            assertEquals(OrderStatus.CANCELLED, order.getStatus());
        }

        @Test
        @DisplayName("Should throw when shipping without confirming")
        void shouldThrowWhenShippingWithoutConfirm() {
            assertThrows(IllegalStateException.class, () -> order.ship());
        }

        @Test
        @DisplayName("Should throw when cancelling delivered order")
        void shouldThrowWhenCancellingDeliveredOrder() {
            order.confirm();
            order.ship();
            order.deliver();
            assertFalse(order.isCancellable());
            assertThrows(IllegalStateException.class, () -> order.cancel());
        }

        @Test
        @DisplayName("Should throw when confirming already confirmed order")
        void shouldThrowWhenConfirmingTwice() {
            order.confirm();
            assertThrows(IllegalStateException.class, () -> order.confirm());
        }
    }
}