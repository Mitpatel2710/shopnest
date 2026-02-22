package com.shopnest.exception;

import com.shopnest.model.OrderStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Hierarchy — Unit Tests")
class ExceptionHierarchyTest {

    @Nested
    @DisplayName("Product Exceptions")
    class ProductExceptions {

        @Test
        @DisplayName("ProductNotFoundException should have correct error code")
        void productNotFoundShouldHaveCorrectCode() {
            ProductNotFoundException ex = new ProductNotFoundException("P001");
            assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
            assertEquals(400,  ex.getHttpStatus());
            assertTrue(ex.getMessage().contains("P001"));
            assertNotNull(ex.getTimestamp());
        }

        @Test
        @DisplayName("OutOfStockException should carry stock details")
        void outOfStockShouldCarryDetails() {
            OutOfStockException ex = new OutOfStockException("P001", 100, 10);
            assertEquals(100, ex.getRequestedQuantity());
            assertEquals(10,  ex.getAvailableQuantity());
            assertEquals(ErrorCode.PRODUCT_OUT_OF_STOCK, ex.getErrorCode());
            assertTrue(ex.getMessage().contains("100"));
            assertTrue(ex.getMessage().contains("10"));
        }

        @Test
        @DisplayName("InvalidProductException should carry validation errors")
        void invalidProductShouldCarryErrors() {
            var errors = java.util.List.of("Name required", "Price negative");
            InvalidProductException ex = new InvalidProductException("P001", errors);
            assertEquals(2, ex.getValidationErrors().size());
            assertTrue(ex.getValidationErrors().contains("Name required"));
        }

        @Test
        @DisplayName("All product exceptions should extend ShopNestException")
        void allProductExceptionsShouldExtendBase() {
            assertInstanceOf(ShopNestException.class, new ProductNotFoundException("P001"));
            assertInstanceOf(ShopNestException.class, new OutOfStockException("P001", 10, 5));
            assertInstanceOf(ShopNestException.class, new DuplicateProductException("P001"));
        }
    }

    @Nested
    @DisplayName("Order Exceptions")
    class OrderExceptions {

        @Test
        @DisplayName("InvalidOrderStateException should carry state details")
        void shouldCarryStateDetails() {
            InvalidOrderStateException ex = new InvalidOrderStateException(
                    "ORD001", OrderStatus.DELIVERED, OrderStatus.CANCELLED
            );
            assertEquals(OrderStatus.DELIVERED,  ex.getCurrentStatus());
            assertEquals(OrderStatus.CANCELLED,  ex.getAttemptedStatus());
            assertTrue(ex.getMessage().contains("ORD001"));
            assertTrue(ex.getMessage().contains("DELIVERED"));
        }

        @Test
        @DisplayName("EmptyCartException should have correct error code")
        void emptyCartShouldHaveCorrectCode() {
            EmptyCartException ex = new EmptyCartException("U001");
            assertEquals(ErrorCode.ORDER_EMPTY_CART, ex.getErrorCode());
            assertTrue(ex.getMessage().contains("U001"));
        }
    }

    @Nested
    @DisplayName("User Exceptions")
    class UserExceptions {

        @Test
        @DisplayName("DuplicateEmailException should carry email")
        void shouldCarryEmail() {
            DuplicateEmailException ex = new DuplicateEmailException("test@shopnest.com");
            assertEquals("test@shopnest.com", ex.getEmail());
            assertEquals(409, ex.getHttpStatus());
            assertEquals(ErrorCode.USER_DUPLICATE_EMAIL, ex.getErrorCode());
        }

        @Test
        @DisplayName("UnauthorizedAccessException should have 403 status")
        void shouldHave403Status() {
            UnauthorizedAccessException ex = new UnauthorizedAccessException("U001", "delete products");
            assertEquals(403, ex.getHttpStatus());
            assertTrue(ex.getMessage().contains("delete products"));
        }
    }

    @Nested
    @DisplayName("Payment Exceptions")
    class PaymentExceptions {

        @Test
        @DisplayName("PaymentFailedException should carry amount and reason")
        void shouldCarryAmountAndReason() {
            PaymentFailedException ex = new PaymentFailedException("ORD001", 89999.0, "Insufficient balance");
            assertEquals(89999.0,               ex.getAmount(), 0.01);
            assertEquals("Insufficient balance", ex.getReason());
            assertEquals(402,                    ex.getHttpStatus());
        }

        @Test
        @DisplayName("InvalidPaymentMethodException should carry method name")
        void shouldCarryMethodName() {
            InvalidPaymentMethodException ex = new InvalidPaymentMethodException("CRYPTO", "ORD001");
            assertEquals("CRYPTO", ex.getMethod());
            assertTrue(ex.getMessage().contains("CRYPTO"));
        }
    }

    @Nested
    @DisplayName("Exception Chaining")
    class ExceptionChaining {

        @Test
        @DisplayName("Should preserve original cause in chain")
        void shouldPreserveCause() {
            RuntimeException cause = new RuntimeException("DB timeout");
            ShopNestException ex   = new ShopNestException(
                    ErrorCode.PRODUCT_NOT_FOUND, "Failed", 500, cause
            );
            assertNotNull(ex.getCause());
            assertEquals("DB timeout", ex.getCause().getMessage());
        }
    }
}
