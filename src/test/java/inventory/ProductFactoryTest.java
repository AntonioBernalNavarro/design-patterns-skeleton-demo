package inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductFactory class.
 * Comprehensive tests for Factory pattern implementation.
 */
class ProductFactoryTest {

    /**
     * Test creating a valid book product.
     */
    @Test
    void testCreateValidBook() {
        Product book = ProductFactory.createProduct("B001", "Java Book", "BOOK", 15.99, 5);

        assertNotNull(book);
        assertEquals("B001", book.getId());
        assertEquals("Java Book", book.getName());
        assertEquals("BOOK", book.getType());
        assertEquals(15.99, book.getPrice(), 0.001);
        assertEquals(5, book.getQuantity());
    }

    /**
     * Test creating a valid electronics product.
     */
    @Test
    void testCreateValidElectronics() {
        Product electronics = ProductFactory.createProduct("E001", "Laptop", "ELECTRONICS", 999.99, 3);

        assertNotNull(electronics);
        assertEquals("E001", electronics.getId());
        assertEquals("Laptop", electronics.getName());
        assertEquals("ELECTRONICS", electronics.getType());
        assertEquals(999.99, electronics.getPrice(), 0.001);
        assertEquals(3, electronics.getQuantity());
    }

    /**
     * Test book minimum price validation.
     */
    @Test
    void testBookMinimumPriceValidation() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Cheap Book", "BOOK", 3.99, 5)
        );

        assertEquals("Books must have minimum price of $5.00", exception.getMessage());
    }

    /**
     * Test electronics minimum price validation.
     */
    @Test
    void testElectronicsMinimumPriceValidation() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("E001", "Cheap Gadget", "ELECTRONICS", 8.99, 3)
        );

        assertEquals("Electronics must have minimum price of $10.00", exception.getMessage());
    }

    /**
     * Test invalid product type.
     */
    @Test
    void testInvalidProductType() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("X001", "Toy", "TOY", 20.0, 5)
        );

        assertTrue(exception.getMessage().contains("Invalid product type"));
        assertTrue(exception.getMessage().contains("TOY"));
    }

    /**
     * Test boundary conditions.
     */
    @Test
    void testBoundaryConditions() {
        // Book at exactly $5.00 — should succeed
        Product bookAtMin = ProductFactory.createProduct("B002", "Min Book", "BOOK", 5.00, 1);
        assertNotNull(bookAtMin);
        assertEquals(5.00, bookAtMin.getPrice(), 0.001);

        // Electronics at exactly $10.00 — should succeed
        Product electronicsAtMin = ProductFactory.createProduct("E002", "Min Electronic", "ELECTRONICS", 10.00, 1);
        assertNotNull(electronicsAtMin);
        assertEquals(10.00, electronicsAtMin.getPrice(), 0.001);
    }

    // ------------------------------------------------------------
    // Additional tests
    // ------------------------------------------------------------

    /**
     * Test with null product type.
     */
    @Test
    void testNullProductType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Book", null, 15.0, 5)
        );
    }

    /**
     * Test with empty product type.
     */
    @Test
    void testEmptyProductType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Book", "", 15.0, 5)
        );
    }

    /**
     * Test with lowercase product type (should fail: strict matching).
     */
    @Test
    void testLowercaseProductTypeIsInvalid() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Book", "book", 15.0, 5)
        );
    }

    /**
     * Test negative quantity propagates from Product constructor.
     */
    @Test
    void testNegativeQuantity() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Book", "BOOK", 15.0, -1)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    /**
     * Test negative price (lower than minimum, but also negative).
     */
    @Test
    void testNegativePrice() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ProductFactory.createProduct("B001", "Book", "BOOK", -1.0, 5)
        );
    }

    /**
     * Test that the returned instance is a Product (not null).
     */
    @Test
    void testReturnedInstanceIsProduct() {
        Product product = ProductFactory.createProduct("B001", "Java Book", "BOOK", 15.99, 5);
        assertInstanceOf(Product.class, product);
    }
}