package com.shopnest.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User — Unit Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("U001", "Rahul", "Sharma", "rahul@shopnest.com", "secure123");
    }

    @Nested
    @DisplayName("Constructor Validation")
    class ConstructorValidation {

        @Test
        @DisplayName("Should create user with valid fields")
        void shouldCreateUserWithValidFields() {
            assertNotNull(user);
            assertEquals("U001",                 user.getId());
            assertEquals("Rahul",                user.getFirstName());
            assertEquals("rahul@shopnest.com",   user.getEmail());
            assertEquals(UserRole.CUSTOMER,      user.getRole());
            assertTrue(user.isActive());
        }

        @Test
        @DisplayName("Should throw when email has no @")
        void shouldThrowForInvalidEmail() {
            assertThrows(IllegalArgumentException.class,
                    () -> new User("U002", "Test", "User", "invalidemail", "pass123"));
        }

        @Test
        @DisplayName("Should throw when password is too short")
        void shouldThrowForShortPassword() {
            assertThrows(IllegalArgumentException.class,
                    () -> new User("U002", "Test", "User", "test@email.com", "123"));
        }

        @Test
        @DisplayName("Should throw when first name is null")
        void shouldThrowForNullFirstName() {
            assertThrows(IllegalArgumentException.class,
                    () -> new User("U002", null, "User", "test@email.com", "pass123"));
        }
    }

    @Nested
    @DisplayName("Role Management")
    class RoleManagement {

        @Test
        @DisplayName("Default role should be CUSTOMER")
        void defaultRoleShouldBeCustomer() {
            assertEquals(UserRole.CUSTOMER, user.getRole());
            assertFalse(user.isAdmin());
            assertFalse(user.isSeller());
        }

        @Test
        @DisplayName("Should promote to SELLER")
        void shouldPromoteToSeller() {
            user.promoteToSeller();
            assertEquals(UserRole.SELLER, user.getRole());
            assertTrue(user.isSeller());
            assertFalse(user.isAdmin());
        }

        @Test
        @DisplayName("Should promote to ADMIN")
        void shouldPromoteToAdmin() {
            user.promoteToAdmin();
            assertEquals(UserRole.ADMIN, user.getRole());
            assertTrue(user.isAdmin());
        }
    }

    @Nested
    @DisplayName("Business Methods")
    class BusinessMethods {

        @Test
        @DisplayName("Should return full name correctly")
        void shouldReturnFullName() {
            assertEquals("Rahul Sharma", user.getFullName());
        }

        @Test
        @DisplayName("Should return full name with null last name")
        void shouldHandleNullLastName() {
            User noLastName = new User("U003", "Priya", null, "priya@test.com", "pass123");
            assertNotNull(noLastName.getFullName());
            assertTrue(noLastName.getFullName().contains("Priya"));
        }

        @Test
        @DisplayName("Should deactivate user")
        void shouldDeactivateUser() {
            user.deactivate();
            assertFalse(user.isActive());
        }
    }
}