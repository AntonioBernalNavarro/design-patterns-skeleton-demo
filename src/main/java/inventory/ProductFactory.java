package inventory;

/**
 * Factory class for creating Product objects with business rules.
 * Implements the Factory pattern with product type validation and pricing rules.
 *
 * Requirements:
 * - Constants for product types
 * - Business rules: Books minimum $5, Electronics minimum $10
 * - Static factory method for product creation
 * - Exception handling for invalid inputs
 */
public class ProductFactory {

    // Product type constants
    public static final String BOOK_TYPE = "BOOK";
    public static final String ELECTRONICS_TYPE = "ELECTRONICS";

    /**
     * Factory method to create products with business rules.
     *
     * @param id       - product identifier
     * @param name     - product name
     * @param type     - product type (must be BOOK or ELECTRONICS)
     * @param price    - product price (must meet minimum requirements)
     * @param quantity - initial stock quantity
     * @return new Product instance
     * @throws IllegalArgumentException if validation fails
     */
    public static Product createProduct(String id, String name, String type, double price, int quantity) {

        // Step 1: Validate product type
        if (!isValidProductType(type)) {
            throw new IllegalArgumentException("Invalid product type: " + type);
        }

        // Step 2: Apply business rules based on product type
        validateBusinessRules(type, price);

        // Step 3: Create and return Product instance
        return new Product(id, name, type, price, quantity);
    }

    // Helper methods for validation

    /**
     * Checks if the given type is a supported product type.
     *
     * @param type - product type to check
     * @return true if type is BOOK or ELECTRONICS, false otherwise
     */
    private static boolean isValidProductType(String type) {
        return BOOK_TYPE.equals(type) || ELECTRONICS_TYPE.equals(type);
    }

    /**
     * Applies business rules: minimum price per product type.
     *
     * @param type  - product type (already validated)
     * @param price - product price
     * @throws IllegalArgumentException if the price doesn't meet the minimum
     */
    private static void validateBusinessRules(String type, double price) {
        if (BOOK_TYPE.equals(type) && price < 5.0) {
            throw new IllegalArgumentException("Books must have minimum price of $5.00");
        }
        if (ELECTRONICS_TYPE.equals(type) && price < 10.0) {
            throw new IllegalArgumentException("Electronics must have minimum price of $10.00");
        }
    }
}