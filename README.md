# 🏦 Banking Management System  
A full-featured Java Desktop Application for managing banking operations using **Java Swing**, **MySQL**, and **FlatLaf**.

---

## 📌 Project Overview

The **Banking Management System** is a desktop-based application designed to simplify core banking functionalities such as customer registration, account handling, balance inquiries, transactions, and user management. It provides a clean, modern UI using FlatLaf and is backed by a relational MySQL database with proper normalization and foreign key constraints.

---

## 🛠️ Technologies Used

| Layer             | Technology         |
|------------------|--------------------|
| UI               | Java Swing + FlatLaf |
| Backend Logic    | Java               |
| Database         | MySQL              |
| Connectivity     | JDBC (Java Database Connectivity) |
| IDE              | NetBeans / IntelliJ |

---

## 🔐 Core Features

- ✅ Secure Login System (Username/Password)
- 👤 Customer Registration with Auto-ID (CS001, CS002, ...)
- 🏦 Account Creation (Saving, Current, Fixed)
- 💰 Deposit & Withdrawal Operations with Live Balance Updates
- 🔁 Money Transfers between Accounts
- 📊 Account Balance Inquiry with Customer Details
- 🧑‍💼 User Management Module (Admin Panel)
- 📄 Customer Report View using JTable
- ❌ Robust Error Handling + Input Validation
- 🔍 SQL JOINs for meaningful data fetching
- ⚙️ Full CRUD Implementation (Create, Read, Update, Delete)

---

## 🧮 Database Schema Overview

- `branch (id, branch)`
- `customer (cust_id, firstname, lastname, city, branch_id, phone)`
- `account (acc_id, cust_id, acc_type, balance)`
- `deposit (acc_id, cust_id, date, balance, deposit)`
- `withdraw (acc_id, cust_id, date, balance, withdraw)`
- `transfer (f_account, to_account, amount)`
- `user (username, name, password)`

🧠 **Normalized to 3NF**  
🔗 **Fully relational with proper PK-FK constraints**

---

## 🧩 How to Run

1. 📥 Clone the repository or copy project files.
2. 🔧 Create the database in MySQL using the provided SQL script.
3. ⚙️ Update JDBC connection string (`localhost`, `database`, `username`, `password`) in `.java` files.
4. ▶️ Run `login.java` or `mainmenu.java` from NetBeans or IntelliJ.
5. 👨‍💻 Login with default user or create a new one.

---

## 📦 Future Improvements

- Password encryption with hashing (e.g., bcrypt)
- Role-based access control (admin, cashier)
- Mobile/Web version (React/Flutter/JavaFX)
- Real-time notifications (email/SMS)
- PDF Report Generation (JasperReports)

---

## 📣 Author

**Asim Hanif**  
`Final Year CS Student | Java Developer | DBMS Enthusiast`

---

## 📧 Contact

💼 [LinkedIn](https://www.linkedin.com/in/masimhanif/)  
📩 Email: your.email@example.com  
💻 GitHub: [your-github](https://github.com/codedbyasim)

---

## ⭐ License

This project is part of an academic submission and can be used for learning or portfolio purposes.

---

