# Simple Inventory Management System

A beginner-friendly Java application demonstrating Factory and Strategy design patterns through a command-line inventory system.

## Features
- Add products (Books, Electronics) using **Factory Pattern**
- Calculate discounts (Student, Bulk, None) using **Strategy Pattern**
- Process sales and track inventory
- View inventory statistics and reports

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Run the Application
```bash
# Method 1: Direct execution
mvn compile
java -cp target/classes inventory.Main

# Method 2: JAR file
mvn package
java -jar target/simple-inventory-1.0.0.jar

# Method 3: Docker
mvn package
docker build -t simple-inventory .
docker run -it simple-inventory
```

## Project Structure
```
src/main/java/inventory/
├── Product.java              # Data model
├── ProductFactory.java       # Factory Pattern
├── DiscountCalculator.java   # Strategy Pattern  
├── InventoryManager.java     # Business logic
└── Main.java                 # CLI interface

src/test/java/inventory/
├── ProductFactoryTest.java
├── DiscountCalculatorTest.java
└── InventoryManagerTest.java
```

## Design Patterns

### Factory Pattern
```java
// Creates products without exposing creation logic
Product book = ProductFactory.createProduct("Book", "Java Guide", 29.99, 10);
Product laptop = ProductFactory.createProduct("Electronics", "Laptop", 799.99, 5);
```

### Strategy Pattern
```java
// Different discount algorithms
double studentDiscount = DiscountCalculator.calculateDiscount(book, 2, "Student");  // 10% off books
double bulkDiscount = DiscountCalculator.calculateDiscount(laptop, 6, "Bulk");      // 15% off 5+ items
```

## Testing
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ProductFactoryTest
```

## Usage
1. Run the application
2. Use menu options:
   - **Add Product** → Demonstrates Factory Pattern
   - **Sell Product** → Demonstrates Strategy Pattern
   - **View Inventory** → See current stock
   - **View Statistics** → Inventory reports

## Learning Objectives
- Implement Factory and Strategy design patterns
- Write unit tests with JUnit 5
- Use Maven for build management
- Deploy with Docker containers
- Follow clean code practices

## Troubleshooting
```bash
# If compilation fails
mvn clean compile -U

# If Docker build fails
mvn clean package
ls target/simple-inventory-1.0.0.jar  # Verify JAR exists
docker build -t simple-inventory .
```
