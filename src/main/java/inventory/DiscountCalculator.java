package inventory;

/**
 * Strategy pattern implementation for calculating different types of discounts.
 *
 * Requirements:
 * - Student discount: 10% off books only
 * - Bulk discount: 15% off when buying 5+ items
 * - No discount option
 * - Return discount amount and description
 */
public class DiscountCalculator {

    /**
     * Inner class to hold discount calculation results.
     */
    public static class DiscountResult {
        private double discountAmount;
        private String description;

        public DiscountResult(double discountAmount, String description) {
            this.discountAmount = discountAmount;
            this.description = description;
        }

        public double getDiscountAmount() { return discountAmount; }
        public String getDescription()    { return description; }
    }

    /**
     * Strategy method for calculating discounts.
     *
     * @param product      - the product being purchased
     * @param quantity     - quantity being purchased
     * @param discountType - type of discount to apply (STUDENT, BULK, NONE)
     * @return DiscountResult with amount and description
     */
    public static DiscountResult calculateDiscount(Product product, int quantity, String discountType) {

        double discountAmount = 0.0;
        String description = "No discount applied";

        switch (discountType.toUpperCase()) {
            case "STUDENT":
                if (isEligibleForStudentDiscount(product)) {
                    discountAmount = product.getPrice() * quantity * 0.10;
                    description = "Student discount: 10% off books";
                } else {
                    description = "Student discount only applies to books";
                }
                break;

            case "BULK":
                if (isEligibleForBulkDiscount(quantity)) {
                    discountAmount = product.getPrice() * quantity * 0.15;
                    description = "Bulk discount: 15% off for 5+ items";
                } else {
                    description = "Bulk discount requires 5+ items";
                }
                break;

            case "NONE":
            default:
                // No discount applied
                break;
        }

        return new DiscountResult(discountAmount, description);
    }

    // Helper methods

    /**
     * Student discount is only applicable to BOOK products.
     *
     * @param product - the product to check
     * @return true if the product is a book
     */
    private static boolean isEligibleForStudentDiscount(Product product) {
        return "BOOK".equals(product.getType());
    }

    /**
     * Bulk discount requires buying 5 or more items.
     *
     * @param quantity - quantity being purchased
     * @return true if quantity >= 5
     */
    private static boolean isEligibleForBulkDiscount(int quantity) {
        return quantity >= 5;
    }
}