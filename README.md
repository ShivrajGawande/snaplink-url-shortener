# 🚀 SnapLink – URL Shortener Backend

SnapLink is a **secure, backend-only URL Shortener application** built using **Spring Boot**.
It enables users to **register, authenticate, shorten URLs**, perform **public redirection**, and view **detailed click analytics**.

This project focuses on **real-world backend engineering**, security, and clean REST API design.

---

## 🛠 Tech Stack

* **Java:** 17
* **Spring Boot:** 3.5.x
* **Spring Security + JWT Authentication**
* **Spring Data JPA**
* **Database:** MySQL
* **Build Tool:** Maven
* **Architecture:** RESTful API

---

## ✨ Features

* User registration & login
* JWT-based authentication (token-only login response)
* Secure URL shortening (authenticated users)
* Public short URL redirection
* Click tracking per URL
* Date-wise analytics
* User-specific URL management
* Role-based authorization (`ROLE_USER`)
* WAR deployment support

---

## 🏗 Project Structure

```text
com.snaplink
 ├── controller
 │    ├── AuthController.java
 │    ├── RedirectController.java
 │    └── UrlMappingController.java
 │
 ├── dto
 │    ├── LoginRequest.java
 │    ├── RegisterRequest.java
 │    ├── UrlMappingDto.java
 │    └── ClickEventDto.java
 │
 ├── models
 │    ├── User.java
 │    ├── UrlMapping.java
 │    └── ClickEvent.java
 │
 ├── repository
 │    ├── UserRepository.java
 │    ├── UrlMappingRepository.java
 │    └── ClickEventRepository.java
 │
 ├── security
 │    ├── WebSecurityConfig.java
 │
 ├── security.jwt
 │    ├── JwtAuthenticationFilter.java
 │    ├── JwtAuthenticationResponse.java
 │    └── JwtUtils.java
 │
 ├── service
 │    ├── UserService.java
 │    ├── UserDetailsServiceImpl.java
 │    ├── UserDetailsImpl.java
 │    └── UrlMappingService.java
 │
 ├── ServletInitializer.java
 └── SnapLinkUrlShortnerApplication.java
```

---

## ▶️ Running the Application

### Prerequisites

* Java 17
* MySQL
* IDE (IntelliJ / Eclipse)

### Steps

1. Clone the repository
2. Create database configuration (see below)
3. Run the application from IDE:

```java
SnapLinkUrlShortnerApplication.java
```

📌 **Run Mode:** From IDE

---

## ⚙️ Database Configuration (Important)

`application.properties` is **not included** in the repository.

After cloning, create:

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

Create a MySQL database named **snaplink** before running the app.

---

## 🔐 JWT Configuration

Add JWT properties to `application.properties`:

```properties
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

---

## 🔐 Authentication APIs

### 1️⃣ Register User

**POST** `/api/auth/public/register`

**Request**

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

**Request**

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

Use the token in headers:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔗 URL Shortening APIs

> 🔒 Requires authentication (`ROLE_USER`)

---

### 3️⃣ Create Short URL

**POST** `/api/urls/shorten`

**Request**

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

---

## 📊 Analytics APIs

### 5️⃣ URL Click Analytics

**GET** `/api/urls/analytics/{shortUrl}`

**Query Params**

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

**Query Params**

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

* Redirects to original URL
* HTTP **302 FOUND**
* Increments click count
* Returns **404** if not found

---

## 🔐 Security Architecture

* JWT-based stateless authentication
* Custom JWT filter validates token on every request
* Role-based access using `@PreAuthorize`
* User details loaded via `UserDetailsServiceImpl`

---

## 🌐 Deployment Support

* Supports **WAR deployment**
* Deployable on external servlet containers (Tomcat, etc.)
* Enabled using `ServletInitializer`

---

## 👨‍💻 Author

**Shivraj Gawande**
Backend Developer | Spring Boot

