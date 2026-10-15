package inventory;

import java.util.*;

/**
 * Main business logic class that manages inventory operations.
 * Implements inventory management using Factory and Strategy patterns.
 *
 * Requirements:
 * - Use ProductFactory for creating products
 * - Use DiscountCalculator for sales with discounts
 * - Maintain product collection
 * - Provide inventory operations and statistics
 */
public class InventoryManager {

    // Collection to store products
    private Map<String, Product> products;

    /**
     * Constructor: initializes the products collection.
     */
    public InventoryManager() {
        this.products = new HashMap<>();
    }

    /**
     * Add product to inventory using Factory pattern.
     *
     * @param id       - product identifier
     * @param name     - product name
     * @param type     - product type
     * @param price    - product price
     * @param quantity - initial quantity
     */
    public void addProduct(String id, String name, String type, double price, int quantity) {
        try {
            Product product = ProductFactory.createProduct(id, name, type, price, quantity);
            products.put(id, product);
            System.out.println("Product added successfully: " + product);
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    /**
     * Sell product with discount using Strategy pattern.
     *
     * @param id           - product identifier
     * @param quantity     - quantity to sell
     * @param discountType - type of discount to apply
     */
    public void sellProduct(String id, int quantity, String discountType) {
        // 1. Find product by ID
        Product product = products.get(id);
        if (product == null) {
            System.out.println("Product not found: " + id);
            return;
        }

        // 2. Validate stock availability
        if (!product.isInStock() || product.getQuantity() < quantity) {
            System.out.println("Insufficient stock for product: " + id);
            return;
        }

        // 3. Calculate discount using DiscountCalculator
        DiscountCalculator.DiscountResult discount =
                DiscountCalculator.calculateDiscount(product, quantity, discountType);

        // 4. Calculate total price and final price
        double totalPrice = product.getPrice() * quantity;
        double finalPrice = totalPrice - discount.getDiscountAmount();

        // 5. Update product quantity
        product.sell(quantity);

        // 6. Display sale information
        displaySalesSummary(product, quantity, totalPrice, finalPrice, discount);
    }

    /**
     * Add stock to existing product.
     *
     * @param id       - product identifier
     * @param quantity - quantity to add
     */
    public void addStock(String id, int quantity) {
        Product product = products.get(id);
        if (product == null) {
            System.out.println("Product not found: " + id);
            return;
        }

        product.addStock(quantity);
        System.out.printf("Added %d units to '%s'. New stock: %d%n",
                quantity, product.getName(), product.getQuantity());
    }

    /**
     * Display all products in inventory.
     */
    public void viewInventory() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("=== Current Inventory ===");
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    /**
     * Calculate total inventory value.
     *
     * @return total value of all products
     */
    public double getInventoryValue() {
        double total = 0.0;
        for (Product product : products.values()) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    /**
     * Get products with low stock.
     *
     * @param threshold - minimum stock level
     * @return list of products below threshold
     */
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> lowStock = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getQuantity() <= threshold) {
                lowStock.add(product);
            }
        }
        return lowStock;
    }

    /**
     * Display inventory statistics.
     */
    public void viewStatistics() {
        System.out.println("=== Inventory Statistics ===");
        System.out.println("Total products: " + products.size());
        System.out.printf("Total inventory value: $%.2f%n", getInventoryValue());

        int threshold = 5;
        List<Product> lowStock = getLowStockProducts(threshold);
        System.out.println("Low stock alerts (threshold = " + threshold + "):");
        if (lowStock.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Product product : lowStock) {
                System.out.printf("  - %s (id=%s, qty=%d)%n",
                        product.getName(), product.getId(), product.getQuantity());
            }
        }
    }

    // Helper methods

    /**
     * Checks whether a product exists by ID.
     *
     * @param id - product identifier
     * @return true if the product exists
     */
    private boolean productExists(String id) {
        return products.containsKey(id);
    }

    /**
     * Displays a formatted sales summary.
     *
     * @param product       - the sold product
     * @param quantity      - quantity sold
     * @param originalPrice - price before discount
     * @param finalPrice    - price after discount
     * @param discount      - discount result (amount + description)
     */
    private void displaySalesSummary(Product product, int quantity,
                                     double originalPrice, double finalPrice,
                                     DiscountCalculator.DiscountResult discount) {
        System.out.println("Sale completed:");
        System.out.printf("Product: %s%n", product.getName());
        System.out.printf("Quantity: %d%n", quantity);
        System.out.printf("Original Price: $%.2f%n", originalPrice);
        System.out.printf("Discount: $%.2f (%s)%n",
                discount.getDiscountAmount(), discount.getDescription());
        System.out.printf("Final Price: $%.2f%n", finalPrice);
        System.out.printf("Remaining stock: %d%n", product.getQuantity());
    }
}