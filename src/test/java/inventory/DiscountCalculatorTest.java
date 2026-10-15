package inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DiscountCalculator class.
 * Comprehensive tests for Strategy pattern implementation.
 */
class DiscountCalculatorTest {

    // Test products
    private Product book;
    private Product electronics;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        book = ProductFactory.createProduct("B001", "Test Book", "BOOK", 20.0, 10);
        electronics = ProductFactory.createProduct("E001", "Test Electronics", "ELECTRONICS", 100.0, 10);
    }

    /**
     * Test student discount on books.
     */
    @Test
    void testStudentDiscountOnBooks() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 2, "STUDENT");

        // 20.0 * 2 * 0.10 = 4.0
        assertEquals(4.0, result.getDiscountAmount(), 0.001);
        assertEquals("Student discount: 10% off books", result.getDescription());
    }

    /**
     * Test student discount on electronics (should not apply).
     */
    @Test
    void testStudentDiscountOnElectronics() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(electronics, 2, "STUDENT");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("Student discount only applies to books", result.getDescription());
    }

    /**
     * Test bulk discount when quantity >= 5.
     */
    @Test
    void testBulkDiscountValid() {
        // Book with qty 5: 20.0 * 5 * 0.15 = 15.0
        DiscountCalculator.DiscountResult bookResult =
                DiscountCalculator.calculateDiscount(book, 5, "BULK");
        assertEquals(15.0, bookResult.getDiscountAmount(), 0.001);
        assertEquals("Bulk discount: 15% off for 5+ items", bookResult.getDescription());

        // Electronics with qty 5: 100.0 * 5 * 0.15 = 75.0
        DiscountCalculator.DiscountResult elecResult =
                DiscountCalculator.calculateDiscount(electronics, 5, "BULK");
        assertEquals(75.0, elecResult.getDiscountAmount(), 0.001);
        assertEquals("Bulk discount: 15% off for 5+ items", elecResult.getDescription());
    }

    /**
     * Test bulk discount when quantity < 5.
     */
    @Test
    void testBulkDiscountInvalid() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 4, "BULK");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("Bulk discount requires 5+ items", result.getDescription());
    }

    /**
     * Test no discount option.
     */
    @Test
    void testNoDiscount() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 3, "NONE");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("No discount applied", result.getDescription());
    }

    /**
     * Test boundary conditions.
     */
    @Test
    void testBoundaryConditions() {
        // Bulk discount with exactly 5 items → applies
        DiscountCalculator.DiscountResult exactFive =
                DiscountCalculator.calculateDiscount(book, 5, "BULK");
        assertEquals(15.0, exactFive.getDiscountAmount(), 0.001);
        assertEquals("Bulk discount: 15% off for 5+ items", exactFive.getDescription());

        // Quantity = 1 → student discount on book still applies
        DiscountCalculator.DiscountResult single =
                DiscountCalculator.calculateDiscount(book, 1, "STUDENT");
        assertEquals(2.0, single.getDiscountAmount(), 0.001);  // 20 * 1 * 0.10

        // Large quantity
        DiscountCalculator.DiscountResult large =
                DiscountCalculator.calculateDiscount(book, 1000, "BULK");
        assertEquals(3000.0, large.getDiscountAmount(), 0.001);  // 20 * 1000 * 0.15
    }

    /**
     * Test discount calculation accuracy.
     */
    @Test
    void testDiscountCalculationAccuracy() {
        // $20 book, qty 2, student = $4.00
        DiscountCalculator.DiscountResult studentOnBook =
                DiscountCalculator.calculateDiscount(book, 2, "STUDENT");
        assertEquals(4.00, studentOnBook.getDiscountAmount(), 0.001);

        // $100 electronics, qty 5, bulk = $75.00
        DiscountCalculator.DiscountResult bulkOnElectronics =
                DiscountCalculator.calculateDiscount(electronics, 5, "BULK");
        assertEquals(75.00, bulkOnElectronics.getDiscountAmount(), 0.001);
    }

    // ------------------------------------------------------------
    // Additional tests
    // ------------------------------------------------------------

    /**
     * Test case sensitivity of discount types (should work in lowercase).
     */
    @Test
    void testDiscountTypeCaseInsensitive() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 2, "student");

        assertEquals(4.0, result.getDiscountAmount(), 0.001);
        assertEquals("Student discount: 10% off books", result.getDescription());
    }

    /**
     * Test invalid discount type (should fall back to no discount).
     */
    @Test
    void testInvalidDiscountType() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 2, "UNKNOWN");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("No discount applied", result.getDescription());
    }

    /**
     * Test with zero quantity (no discount should be calculated).
     */
    @Test
    void testZeroQuantity() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 0, "STUDENT");

        // discount = 20 * 0 * 0.10 = 0
        assertEquals(0.0, result.getDiscountAmount(), 0.001);
    }

    /**
     * Test that null discountType throws NullPointerException (toUpperCase on null).
     */
    @Test
    void testNullDiscountType() {
        assertThrows(
            NullPointerException.class,
            () -> DiscountCalculator.calculateDiscount(book, 2, null)
        );
    }
}