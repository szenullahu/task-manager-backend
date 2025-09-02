# Task Manager Backend

This backend is designed to work with a separate Angular frontend, which consumes these APIs to provide a complete task management application.  
You can find the frontend repository [here](https://github.com/szenullahu/task-manager-frontend).

--- 

## Tech Stack 

- 🟨 **Java 21**
- 🚀 **Spring Boot 3.5.3**
- 🔐 **Spring Security + JWT**
- 📦 **Maven**
- 🗃️ **PostgreSQL**
- 💾 **JPA / Hibernate**
- 🧾 **Lombok**
- ✅ **Bean Validation (Jakarta)**
- 🧪 **Postman** – for testing and exploring the API

--- 

## 🚀 Getting Started

### ✅ Requirements

Make sure the following tools are installed on your machine:

- 🔸 [Java 21 (JDK)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- 🔸 [Maven](https://maven.apache.org/)
- 🔸 [PostgreSQL 15](https://www.postgresql.org/download) – must be installed and running locally on Port 5432 
- 🔸 [Postman](https://www.postman.com/downloads/) - optional, for testing the REST API

--- 

### 💾 Postgres Setup

1. Start your local PostgreSQL server
2. Create a new database named `task-manager`
3. Create a file at `src/main/resources/application.properties` using the provided template `application.properties.example`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task-manager
spring.datasource.username={username}
spring.datasource.password={password}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

```

--- 

### ▶️ Run the Application


To start the backend application, run the following commands in the project root:

```bash
mvn clean install
mvn spring-boot:run

```

The first command builds the project, the second one starts the Spring Boot application.

Once started, the backend will listen on: 
📍 http://localhost:8080

--- 

## 🧪 Testing the API (with Postman)

You can test the API using [Postman](https://www.postman.com/downloads/). Here's a typical workflow for registering, logging in, and calling protected endpoints.

---

### 🔐 Register a new user

**POST** `http://localhost:8080/api/user/register`  
**Body (JSON):**
```json
{
  "username": "newuser123",
  "email": "newuser@example.com",
  "password": "12345678",
  "firstname": "John",
  "surname": "Doe"
}
```

**Response:**

**Body (JSON):**
```json
{
  "username": "newuser123",
  "email": "newuser@example.com",
  "firstname": "John",
  "surname": "Doe"
}
```

--- 

###  🔑 Login 

**POST** `http://localhost:8080/api/user/login`  
**Body (JSON):**
```json
{
  "username": "newuser123",
  "password": "12345678"
}
```

**Response:**

**Body (JSON):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
``` 

---

### 🔐 Use token for protected requests 

Copy the value of the **token** from the login response and add the following HTTP header to all secured endpoints 

```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

```

In Postman: 
* Go to the **Authorization tab** 
* Type: **Bearer Token**
* Paste your **token**

--- 

## 📡 API Endpoints

### 👤 User Endpoints

| Method | Endpoint                     | Description                                               | Auth Required |
|--------|------------------------------|-----------------------------------------------------------|----------------|
| GET    | `/api/user`                  | Get current user info                                     | ✅             |
| POST   | `/api/user/register`         | Register a new user                                       | ❌             |
| POST   | `/api/user/login`            | Log in and receive JWT                                    | ❌             |
| PUT    | `/api/user/me`               | Update user info (email, firstname, surname)              | ✅             |
| PUT    | `/api/user/me/password`      | Change password (requires old & new password)             | ✅             |
| DELETE | `/api/user/me`               | Delete user account                                       | ✅             |

---

### ✅ Task Endpoints

| Method | Endpoint               | Description                                               | Auth Required |
|--------|------------------------|-----------------------------------------------------------|----------------|
| GET    | `/api/tasks`           | Get all tasks of the current user                         | ✅             |
| GET    | `/api/tasks/{id}`      | Get a specific task by ID                                 | ✅             |
| POST   | `/api/tasks`           | Create a new task (title, description, status, etc.)      | ✅             |
| PUT    | `/api/tasks/{id}`      | Update a task (title, description, status, etc.)          | ✅             |
| DELETE | `/api/tasks/{id}`      | Delete a task by ID                                       | ✅             |



