package com.shopnest.service;

import com.shopnest.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiscountService — Unit Tests")
class DiscountServiceTest {

    private DiscountService discountService;
    private BaseProduct product;
    private User customer;
    private User seller;
    private User admin;

    @BeforeEach
    void setUp() {
        product         = new ElectronicsProduct("E001", "iPad", "Tablet", 50000.0, 10, "Apple", 12);
        customer        = new User("U001", "Rahul", "Sharma", "rahul@test.com", "pass123");
        seller          = new User("U002", "Priya", "Patel",  "priya@test.com", "pass123");
        admin           = new User("U003", "Admin", "User",   "admin@test.com", "pass123");
        discountService = new DiscountService(new NoDiscountStrategy());
        seller.promoteToSeller();
        admin.promoteToAdmin();
    }

    @Test
    @DisplayName("No discount — should return original price")
    void noDiscountShouldReturnOriginalPrice() {
        discountService.setStrategy(new NoDiscountStrategy());
        double price = discountService.getFinalPrice(product, customer);
        assertEquals(50000.0, price, 0.01);
    }

    @Test
    @DisplayName("Seasonal discount — should apply percent correctly")
    void seasonalDiscountShouldApplyCorrectly() {
        discountService.setStrategy(new SeasonalDiscountStrategy("Diwali", 20.0));
        double price = discountService.getFinalPrice(product, customer);
        assertEquals(40000.0, price, 0.01); // 50000 - 20%
    }

    @Test
    @DisplayName("Loyalty — CUSTOMER gets 5% discount")
    void loyaltyCustomerShouldGet5Percent() {
        discountService.setStrategy(new LoyaltyDiscountStrategy());
        double price = discountService.getFinalPrice(product, customer);
        assertEquals(47500.0, price, 0.01); // 50000 - 5%
    }

    @Test
    @DisplayName("Loyalty — SELLER gets 15% discount")
    void loyaltySellerShouldGet15Percent() {
        discountService.setStrategy(new LoyaltyDiscountStrategy());
        double price = discountService.getFinalPrice(product, seller);
        assertEquals(42500.0, price, 0.01); // 50000 - 15%
    }

    @Test
    @DisplayName("Loyalty — ADMIN gets 20% discount")
    void loyaltyAdminShouldGet20Percent() {
        discountService.setStrategy(new LoyaltyDiscountStrategy());
        double price = discountService.getFinalPrice(product, admin);
        assertEquals(40000.0, price, 0.01); // 50000 - 20%
    }

    @Test
    @DisplayName("Valid coupon — should apply correct discount")
    void validCouponShouldApplyDiscount() {
        CouponDiscountStrategy coupon = new CouponDiscountStrategy("SAVE20");
        assertTrue(coupon.isValidCoupon());
        discountService.setStrategy(coupon);
        double price = discountService.getFinalPrice(product, customer);
        assertEquals(40000.0, price, 0.01); // 50000 - 20%
    }

    @Test
    @DisplayName("Invalid coupon — should apply zero discount")
    void invalidCouponShouldApplyZeroDiscount() {
        CouponDiscountStrategy fakeCoupon = new CouponDiscountStrategy("FAKE99");
        assertFalse(fakeCoupon.isValidCoupon());
        discountService.setStrategy(fakeCoupon);
        double price = discountService.getFinalPrice(product, customer);
        assertEquals(50000.0, price, 0.01); // no discount
    }

    @Test
    @DisplayName("Strategy should be swappable at runtime")
    void strategyShouldBeSwappableAtRuntime() {
        discountService.setStrategy(new NoDiscountStrategy());
        assertEquals(50000.0, discountService.getFinalPrice(product, customer), 0.01);

        discountService.setStrategy(new SeasonalDiscountStrategy("Sale", 50.0));
        assertEquals(25000.0, discountService.getFinalPrice(product, customer), 0.01);
    }
}