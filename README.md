# 🧾 Order Management System

A learning-focused Order Management System developed in **Java**, implementing core backend concepts through a practical, real-world business scenario.

---

## 📋 Table of Contents

- [Purpose](#purpose)
- [Features](#features)
- [Architecture](#architecture)
- [Business Rules](#business-rules)
- [Installation & Setup](#installation--setup)
- [Tests](#tests)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Learning Outcomes](#learning-outcomes)
- [Future Improvements](#future-improvements)

---

## 🎯 Purpose

This project serves as a **backend development foundation**, focusing on:

- **Object-Oriented Programming (OOP)** — Encapsulation, inheritance, and polymorphism
- **Domain Modeling** — Designing entities and relationships
- **Business Logic** — Implementing real-world rules and workflows
- **Data Structures** — Managing complex data relationships
- **Application Flow** — User interaction and state management

**Goal:** Build a strong Java foundation before advancing to frameworks like Spring Boot.

---

## ⚙️ Features

- ✅ **Customer Management** — Create and list customers
- ✅ **Product Catalog** — Create and manage products  
- ✅ **Order Management** — Create orders and add items
- ✅ **Smart Calculations** — Automatic order value computation
- ✅ **Order Tracking** — Monitor order status through lifecycle
- ✅ **Order History** — View all orders and their details
- ✅ **Interactive Menu** — User-friendly console interface
- ✅ **Commands** — Menu actions encapsulated via Command pattern
- ✅ **Unit Tests** — Service-level JUnit tests

---

## 🧱 Architecture

### Core Entities

| Entity | Purpose |
|--------|---------|
| **Customer** | Represents the buyer with contact information |
| **Product** | Available items for sale with pricing |
| **Order** | Contains customer reference, items, and lifecycle status |
| **OrderItem** | Bridges Product and Order (tracks quantity + subtotal) |
| **OrderStatus** | Enum controlling the order lifecycle |

### Entity Relationships

```
Customer (1) ──── (many) Order
Order (1) ──── (many) OrderItem
Product (1) ──── (many) OrderItem
```

---

## 🧠 Business Rules

- Every order must belong to exactly one customer
- An order can contain multiple items (products)
- Each item tracks product, quantity, and calculated subtotal
- **Order total is calculated automatically** by summing all item subtotals
- Order status follows a defined lifecycle (e.g., PENDING → PROCESSING → SHIPPED → DELIVERED / CANCELED)
- Only valid orders with items can be created
- Order status updates are tracked throughout the order lifecycle

---

## 📦 Installation & Setup

### Prerequisites

- **Java 11** or higher
- **IDE**: IntelliJ IDEA, Eclipse, VS Code (with Extension Pack for Java), or any preferred IDE
- **Maven** (optional if using IDE Maven integration)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/GustavoHRdev/Order-Management-System.git
   cd Order-Management-System
   ```

2. **Open in your IDE**
   - IntelliJ IDEA: File → Open → Select folder
   - Eclipse: File → Import → Existing Projects into Workspace
   - VS Code: File → Open Folder

3. **Run the application**
   - Locate `src/main/java/app/Main.java`
   - Right-click → Run (or press Ctrl+Shift+F10 in IntelliJ)

---

## 🚀 Usage

Once the application starts, you'll see an interactive menu:

```
1 - Cadastrar cliente
2 - Cadastrar produto
3 - Criar pedido
4 - Listar pedidos
5 - Atualizar status do pedido
6 - Listar clientes
7 - Sair
Escolha uma opção: _
```

### Example Workflow

**Step 1:** Register a customer
- Choose option `1`
- Enter customer name and email

**Step 2:** Register products
- Choose option `2`
- Enter product name and price
- Repeat to add multiple products

**Step 3:** Create an order
- Choose option `3`
- Select a customer
- Add products with quantities
- Order total is calculated automatically

**Step 4:** Track orders
- Choose option `4` to view all orders with items and status
- Choose option `5` to update order status

---

## 🧪 Tests

Run the tests with Maven:

```bash
mvn test
```

You can also run tests directly from IntelliJ (right-click a test class or use the Maven tool window).

---

## 📂 Project Structure

```
Order-Management-System/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── app/              # Application entry point
│   │       ├── cli/              # Console UI and commands
│   │       ├── model/            # Core domain classes
│   │       ├── repository/       # In-memory repositories
│   │       └── service/          # Business logic
│   └── test/
│       └── java/
│           └── service/          # Unit tests
├── out/                          # Compiled classes
├── pom.xml                       # Maven build
├── README.md                     # This file
└── Order-Management-System.iml   # IntelliJ project file
```

---

## 💡 Learning Outcomes

By studying this project, you'll understand:

1. **OOP Principles** — Classes, objects, inheritance, and encapsulation
2. **Domain Modeling** — How to structure entities and their relationships
3. **Business Logic** — Implementing rules like order calculations
4. **Collections** — Using ArrayList, HashMaps for data management
5. **Enums** — Type-safe constants for order status
6. **User Interaction** — Building console-based interfaces
7. **State Management** — Tracking object state changes

---

## 🚀 Future Improvements

- 🔹 **Input Validation** — Stronger validation and user feedback
- 🔹 **Better Formatting** — Currency formatting (e.g., R$ 1.234,56)
- 🔹 **Enhanced UX** — Improved menu navigation and error messages
- 🔹 **Data Persistence** — Save/load from files or database
- 🔹 **Layered Architecture** — Stronger boundaries and DTOs
- 🔹 **Error Handling** — Custom exceptions and error codes
- 🔹 **Order Analytics** — Reports and statistics

---

## 📌 Notes

This is a **learning project** focused on building fundamentals before moving to Spring Boot. The code prioritizes clarity and educational value over production-ready patterns.

Feel free to fork, modify, and use as a reference for your own backend learning journey! 🎓
