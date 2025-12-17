
# 🚀 SnapLink – URL Shortener Backend (Spring Boot)

SnapLink is a **secure, backend-only URL Shortener application** built using **Spring Boot**.
It allows users to **register, authenticate, shorten URLs, redirect short links**, and **track click analytics** with date-wise statistics.

This project focuses purely on **backend engineering**, security, and clean API design — no frontend included.

---

## 🛠 Tech Stack

* **Java:** 17
* **Spring Boot:** 3.5.x
* **Spring Security (JWT Authentication)**
* **Spring Data JPA**
* **Database:** MySQL
* **Build Tool:** Maven
* **Architecture:** RESTful API

---

## ✨ Features

* User registration & login
* JWT-based authentication
* Secure URL shortening
* Public short URL redirection
* Click tracking per URL
* Date-wise analytics
* User-specific URL management
* Role-based authorization (`ROLE_USER`)

---

## 🏗 Project Structure

```
com.snaplink
 ├── controller
 │    ├── AuthController
 │    ├── UrlMappingController
 │    └── RedirectController
 ├── dto
 │    ├── LoginRequest
 │    ├── RegisterRequest
 │    ├── UrlMappingDto
 │    └── ClickEventDto
 ├── models
 │    ├── User
 │    └── UrlMapping
 ├── service
 │    ├── UserService
 │    └── UrlMappingService
 └── SnapLinkUrlShortnerApplication.java
```
Good catch 👍
Yes — since **`application.properties` is not present in the repo**, we should **explicitly mention this in the README** so anyone cloning it knows what to do.

Below is a **small, clean section** you should **add to your README** (recommended place: under **“Running the Application”**).

You can paste this **exactly as-is**.

---

## ⚙️ Database Configuration (Important)

This project does **not** include `application.properties` in the repository.

After cloning the project, **create the file manually**:

```
src/main/resources/application.properties
```

### Example MySQL Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/snaplink
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

spring.jpa.properties.hibernate.format_sql=true
```

📌 **Notes**

* Create a MySQL database named `snaplink`
* Update username & password as per your local setup
* Tables will be auto-created on application startup

---

## 🔐 JWT Configuration

Add the following JWT properties to `application.properties`:

```properties
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

---

## 🧠 Why This Is a Good Practice

* Keeps **credentials out of version control**
* Prevents accidental leaks
* Follows **industry-standard security practices**

---

## ▶️ Running the Application

### Prerequisites

* Java 17 installed
* MySQL running
* IDE (IntelliJ / Eclipse)

### Steps

1. Clone the repository
2. Configure MySQL in `application.properties`
3. Run the application from IDE using:

```java
SnapLinkUrlShortnerApplication.java
```

📌 **Run mode:** From IDE

---

## 🔐 Authentication APIs

### 1️⃣ Register User

**POST** `/api/auth/public/register`

**Request Body**

```json
{
  "username": "shivraj_",
  "email": "shivraj@email.com",
  "password": "password123"
}
```

**Response**

```
User Registred Successfully
```

---

### 2️⃣ Login User

**POST** `/api/auth/public/login`

**Request Body**

```json
{
  "username": "shivraj_",
  "password": "password123"
}
```

**Response (JWT Token Only)**

```json
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGl2cmFqXyIsImlhdCI6MT..."
```

📌 Use this token for secured APIs:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔗 URL Shortening APIs

> 🔒 Requires authentication (`ROLE_USER`)

---

### 3️⃣ Create Short URL

**POST** `/api/urls/shorten`

**Request Body**

```json
{
  "originalUrl": "https://www.amazon.com"
}
```

**Response**

```json
{
  "id": 5,
  "originalUrl": "https://www.amazon.com",
  "shortUrl": "J72ksYN6",
  "clickCount": 0,
  "createdDate": "2025-12-17T23:00:27.4148759",
  "username": "shubhra_10"
}
```

---

### 4️⃣ Get Logged-In User URLs

**GET** `/api/urls/myurls`

**Response**

```json
[
  {
    "id": 1,
    "originalUrl": "https://www.google.com",
    "shortUrl": "ERgbxr4W",
    "clickCount": 0,
    "createdDate": "2025-12-15T02:16:49.982062",
    "username": "shivraj_"
  },
  {
    "id": 2,
    "originalUrl": "https://www.facebook.com",
    "shortUrl": "4jjCwKj0",
    "clickCount": 0,
    "createdDate": "2025-12-16T00:11:50.673189",
    "username": "shivraj_"
  }
]
```

📌 Returns URLs **created by the logged-in user only**

---

## 📊 Analytics APIs

---

### 5️⃣ URL Click Analytics

**GET** `/api/urls/analytics/{shortUrl}`

**Query Parameters**

```
startDate=2025-12-01T00:00:00
endDate=2025-12-31T23:59:59
```

**Response**

```json
[
  {
    "clickDate": "2025-12-17",
    "count": 12
  },
  {
    "clickDate": "2025-12-18",
    "count": 7
  }
]
```

---

### 6️⃣ Total Clicks (User-wise)

**GET** `/api/urls/totalClicks`

**Query Parameters**

```
startDate=2025-12-01
endDate=2025-12-31
```

**Response**

```json
{
  "2025-12-15": 10,
  "2025-12-16": 22,
  "2025-12-17": 5
}
```

---

## 🌍 Public Redirect API

### 7️⃣ Redirect Short URL

**GET** `/{shortUrl}`

**Behavior**

* Redirects to original URL
* HTTP Status: **302 FOUND**
* Increments click count
* Returns **404** if short URL does not exist

---

## 📦 DTO Reference

### LoginRequest

```java
public class LoginRequest {
    private String username;
    private String password;
}
```

### UrlMappingDto

```java
public class UrlMappingDto {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
}
```

### ClickEventDto

```java
public class ClickEventDto {
    private LocalDate clickDate;
    private Long count;
}
```

---

## 🔒 Security

* JWT-based authentication
* Role-based authorization
* Public endpoints:

  * `/api/auth/public/**`
  * `/{shortUrl}`
* Protected endpoints:

  * `/api/urls/**`
---

## 👨‍💻 Author

**Shivraj Gawande**
Backend Developer | Spring Boot
📌 URL Shortener Backend Project

---

