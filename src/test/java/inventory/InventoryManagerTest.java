package inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for InventoryManager class.
 * Comprehensive tests for inventory management operations.
 */
class InventoryManagerTest {

    // Test instance
    private InventoryManager manager;

    /**
     * Set up fresh instance before each test.
     */
    @BeforeEach
    void setUp() {
        manager = new InventoryManager();
    }

    /**
     * Test adding products to inventory.
     */
    @Test
    void testAddProduct() {
        manager.addProduct("B001", "Test Book", "BOOK", 20.0, 5);

        // Verify inventory value reflects the added product
        assertEquals(100.0, manager.getInventoryValue(), 0.001);

        // Verify the product is present (using a high threshold to list all)
        List<Product> all = manager.getLowStockProducts(Integer.MAX_VALUE);
        assertEquals(1, all.size());
        assertEquals("B001", all.get(0).getId());
    }

    /**
     * Test adding product with invalid parameters.
     */
    @Test
    void testAddInvalidProduct() {
        // Invalid price (below minimum for BOOK)
        manager.addProduct("B001", "Cheap Book", "BOOK", 3.99, 5);

        // Invalid type
        manager.addProduct("X001", "Toy", "TOY", 20.0, 5);

        // Inventory should remain empty
        assertEquals(0.0, manager.getInventoryValue(), 0.001);
        assertTrue(manager.getLowStockProducts(Integer.MAX_VALUE).isEmpty());
    }

    /**
     * Test selling products.
     */
    @Test
    void testSellProduct() {
        manager.addProduct("B001", "Test Book", "BOOK", 20.0, 5);

        // Sell 2 with no discount → remaining 3 → value = 60.0
        manager.sellProduct("B001", 2, "NONE");
        assertEquals(60.0, manager.getInventoryValue(), 0.001);

        // Sell 1 with STUDENT discount (10% off book) → remaining 2
        manager.sellProduct("B001", 1, "STUDENT");
        assertEquals(40.0, manager.getInventoryValue(), 0.001);
    }

    /**
     * Test selling more than available stock.
     */
    @Test
    void testSellInsufficientStock() {
        manager.addProduct("B001", "Test Book", "BOOK", 20.0, 5);

        // Try to sell 10 (only 5 available) → rejected
        manager.sellProduct("B001", 10, "NONE");

        // Stock should remain unchanged
        assertEquals(100.0, manager.getInventoryValue(), 0.001);

        List<Product> all = manager.getLowStockProducts(Integer.MAX_VALUE);
        assertEquals(5, all.get(0).getQuantity());
    }

    /**
     * Test adding stock to existing product.
     */
    @Test
    void testAddStock() {
        manager.addProduct("B001", "Test Book", "BOOK", 20.0, 5);

        manager.addStock("B001", 3);

        // Value should be 20 * 8 = 160
        assertEquals(160.0, manager.getInventoryValue(), 0.001);

        List<Product> all = manager.getLowStockProducts(Integer.MAX_VALUE);
        assertEquals(8, all.get(0).getQuantity());
    }

    /**
     * Test inventory value calculation.
     */
    @Test
    void testInventoryValue() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.0, 5);       // 100.0
        manager.addProduct("B002", "Design Patterns", "BOOK", 30.0, 3); // 90.0
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 500.0, 2);  // 1000.0

        assertEquals(1190.0, manager.getInventoryValue(), 0.001);
    }

    /**
     * Test low stock detection.
     */
    @Test
    void testLowStockProducts() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.0, 3);       // low (<=5)
        manager.addProduct("B002", "Design Patterns", "BOOK", 30.0, 5); // low (<=5)
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 500.0, 10); // OK
        manager.addProduct("E002", "Mouse", "ELECTRONICS", 20.0, 1);    // low (<=5)

        List<Product> low = manager.getLowStockProducts(5);
        assertEquals(3, low.size());

        // Verify the IDs of the low stock products
        List<String> ids = low.stream().map(Product::getId).toList();
        assertTrue(ids.contains("B001"));
        assertTrue(ids.contains("B002"));
        assertTrue(ids.contains("E002"));
        assertFalse(ids.contains("E001"));
    }

    /**
     * Test operations on non-existent products.
     */
    @Test
    void testNonExistentProduct() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.0, 5);

        // Try to sell a non-existent product
        manager.sellProduct("NOPE", 1, "NONE");

        // Try to add stock to a non-existent product
        manager.addStock("NOPE", 10);

        // Inventory should remain unchanged
        assertEquals(100.0, manager.getInventoryValue(), 0.001);
        assertEquals(1, manager.getLowStockProducts(Integer.MAX_VALUE).size());
    }

    /**
     * Test complete workflow.
     */
    @Test
    void testCompleteWorkflow() {
        // 1. Add products
        manager.addProduct("B001", "Java Book", "BOOK", 20.0, 10);       // 200.0
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 500.0, 3);   // 1500.0
        assertEquals(1700.0, manager.getInventoryValue(), 0.001);

        // 2. Sell a book with student discount
        manager.sellProduct("B001", 2, "STUDENT");  // remaining 8 → 160.0
        assertEquals(1660.0, manager.getInventoryValue(), 0.001);

        // 3. Sell a laptop with bulk discount
        manager.sellProduct("E001", 3, "BULK");     // remaining 0 → 0.0
        assertEquals(160.0, manager.getInventoryValue(), 0.001);

        // 4. Restock the book
        manager.addStock("B001", 5);                // 13 → 260.0
        assertEquals(260.0, manager.getInventoryValue(), 0.001);

        // 5. Verify low stock (threshold = 5): only B001 (qty 13) is above? No, B001 = 13 → not low
        // Electronics E001 has 0 → low
        List<Product> low = manager.getLowStockProducts(5);
        assertEquals(1, low.size());
        assertEquals("E001", low.get(0).getId());
    }

    // ------------------------------------------------------------
    // Additional tests
    // ------------------------------------------------------------

    /**
     * Test selling with STUDENT discount on an electronics (no discount).
     */
    @Test
    void testSellElectronicsWithStudentDiscount() {
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 500.0, 3);

        // Student discount only applies to books → full price
        manager.sellProduct("E001", 1, "STUDENT");

        // Remaining 2 → 1000.0
        assertEquals(1000.0, manager.getInventoryValue(), 0.001);
    }

    /**
     * Test selling with BULK discount below threshold.
     */
    @Test
    void testSellWithBulkBelowThreshold() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.0, 10);

        // BULK requires 5+ items → no discount applied
        manager.sellProduct("B001", 3, "BULK");

        // Remaining 7 → 140.0
        assertEquals(140.0, manager.getInventoryValue(), 0.001);
    }

    /**
     * Test with large inventory.
     */
    @Test
    void testLargeInventory() {
        for (int i = 0; i < 100; i++) {
            manager.addProduct("B" + i, "Book " + i, "BOOK", 10.0, 10);
        }

        // 100 products × 10 units × $10 = $10,000
        assertEquals(10000.0, manager.getInventoryValue(), 0.001);
        assertEquals(100, manager.getLowStockProducts(Integer.MAX_VALUE).size());
    }

    /**
     * Test empty inventory behavior.
     */
    @Test
    void testEmptyInventory() {
        assertEquals(0.0, manager.getInventoryValue(), 0.001);
        assertTrue(manager.getLowStockProducts(5).isEmpty());
        assertTrue(manager.getLowStockProducts(Integer.MAX_VALUE).isEmpty());
    }

    /**
     * Test boundary: threshold exactly equals stock quantity.
     */
    @Test
    void testLowStockBoundary() {
        manager.addProduct("B001", "Book A", "BOOK", 20.0, 5);
        manager.addProduct("B002", "Book B", "BOOK", 20.0, 6);

        // Threshold 5: B001 (qty 5) qualifies (<= 5), B002 (qty 6) does not
        List<Product> low = manager.getLowStockProducts(5);
        assertEquals(1, low.size());
        assertEquals("B001", low.get(0).getId());
    }
}