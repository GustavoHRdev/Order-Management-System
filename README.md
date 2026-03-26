# 🧾 Order Management System
Backend application for order management developed in Java, focusing on domain modeling, business rules implementation, and clean code organization.

Designed to simulate real-world order processing scenarios with structured business logic.

---

## 🚀 Features

- Customer management (create and list customers)
- Product catalog management
- Order creation with multiple items
- Automatic order total calculation
- Order lifecycle management (status updates)
- Order history visualization
- Unit tests for business logic validation

---

## 🧱 Architecture

The application is structured using a layered approach:

- **Model** → Domain entities and relationships  
- **Service** → Business rules and application logic  
- **Repository** → In-memory data management  
- **CLI (Interface)** → User interaction via console  

---

## 📊 Domain Model

Main entities:

- **Customer** → Represents the client  
- **Product** → Represents available items  
- **Order** → Contains customer, items, and status  
- **OrderItem** → Associates products with orders (quantity and subtotal)  
- **OrderStatus (Enum)** → Controls order lifecycle  

### Relationships

- Customer (1) → (many) Orders  
- Order (1) → (many) OrderItems  
- Product (1) → (many) OrderItems  

---

## 🧠 Business Rules

- Each order must be associated with a customer  
- Orders must contain at least one item  
- Each item stores product, quantity, and subtotal  
- Total order value is calculated automatically  
- Order status follows a defined lifecycle (e.g., PENDING → PROCESSING → SHIPPED → DELIVERED / CANCELED)  

---

## 🛠 Tech Stack

- Java  
- Object-Oriented Programming (OOP)  
- Maven  
- JUnit (unit testing)  
- Collections Framework (List, Map)

---

## ▶️ Running the Application

### Requirements

- Java 11+

### Steps

```bash
git clone https://github.com/GustavoHRdev/Order-Management-System.git
cd Order-Management-System
```
Run the application:

Open the project in your IDE
Run Main.java

--- 
## 🧪 Tests

Run with Maven:
```bash
mvn test
```
Or execute directly via IDE.
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

This project emphasizes domain modeling, business rules, and structured code organization.

Next steps include evolving it into a REST API using Spring Boot with database integration.
