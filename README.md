# ☕ JavaReady — Java Interview Preparation & Quiz Platform

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Hibernate-7.2-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate" />
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" alt="License" />
</p>

JavaReady is a production-grade backend application engineered for Java developers and job seekers preparing for technical coding and conceptual interviews. It features categorized interview questions, code snippets, search & filtering, and an automated interactive quiz attempt and evaluation engine.

---

## 🏛️ System Architecture

```
                               ┌────────────────────────┐
                               │     Client Layers      │
                               │ (Web / Mobile / Ext)   │
                               └───────────┬────────────┘
                                           │ HTTP / JSON
                                           ▼
                               ┌────────────────────────┐
                               │   Spring MVC Layer     │
                               │  REST API Controllers  │
                               └───────────┬────────────┘
                                           │
                                           ▼
                               ┌────────────────────────┐
                               │     Service Layer      │
                               │ Business & Quiz Logic  │
                               └───────────┬────────────┘
                                           │
                                           ▼
                               ┌────────────────────────┐
                               │   Spring Data JPA      │
                               │ Hibernate Repositories │
                               └───────────┬────────────┘
                                           │ JDBC / SQL
                                           ▼
                               ┌────────────────────────┐
                               │    MySQL Database      │
                               │  (Aiven Cloud / Local) │
                               └────────────────────────┘
```

---

## 🗄️ Database Entity-Relationship Model

```
 ┌─────────────────┐       1:N       ┌──────────────────┐
 │    categories   │────────────────►│    questions     │
 └─────────────────┘                 └─────────┬────────┘
          ▲                                    │ 1:N
          │ 1:N                                │
 ┌────────┴────────┐       1:N       ┌─────────▼────────┐
 │    attempts     │────────────────►│  attempt_answers │
 └────────┬────────┘                 └──────────────────┘
          │ N:1
          ▼
 ┌─────────────────┐
 │      users      │
 └─────────────────┘
```

### Table Definitions

| Table | Purpose | Key Columns |
| :--- | :--- | :--- |
| `categories` | Categorizes questions by topic (e.g. OOP, JVM, Concurrency) | `id`, `name`, `description`, `active`, `display_order`, `created_at`, `updated_at` |
| `questions` | Detailed interview questions with code and explanations | `id`, `title`, `question_text`, `code`, `explanation`, `difficulty`, `tags`, `active`, `category_id`, `created_by`, `created_at`, `updated_at` |
| `attempts` | Records quiz sessions, overall scores, and duration | `id`, `user_id`, `category_id`, `total_questions`, `correct_answers`, `wrong_answers`, `score`, `status`, `started_at`, `completed_at` |
| `attempt_answers`| Question-by-question candidate response tracking | `id`, `attempt_id`, `question_id`, `selected_answer`, `correct_answer`, `is_correct` |
| `users` | Candidate & Admin user accounts | `id`, `name`, `email`, `password`, `role`, `profile_image_url`, `bio`, `created_at`, `updated_at` |

---

## 🚀 Key Features

* **Categorized Topic Navigation**: Dynamic topic ordering, active/inactive toggles, and metadata tracking.
* **Rich Interview Questions**: Questions with syntax-highlighted code snippets, deep explanations, difficulty levels (`EASY`, `MEDIUM`, `HARD`), and search tags.
* **Full-Text Search & Multi-Criteria Filtering**: Filter questions by Category ID, Difficulty, and partial matching on Title, Question Text, or Tags with Spring Data pagination.
* **Interactive Quiz Attempt & Scoring Engine**:
  * Batch submission of answers.
  * Real-time automated scoring and accuracy percentage calculation.
  * Comprehensive feedback showing the candidate's answer alongside the expert explanation and correctness flag.
* **Robust Error Handling**: Standardized REST error envelopes (`status`, `error`, `message`, `path`, `timestamp`) with `GlobalExceptionHandler` and `ResourceNotFoundException`.
* **Clean Layered Architecture**: DTO encapsulation, transactional service implementations, and Spring JPA interfaces.

---

## 📡 REST API Reference

### 1. Categories (`/api/categories`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/categories` | Get all active categories ordered by display order |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `POST` | `/api/categories` | Create a new category |
| `PUT` | `/api/categories/{id}` | Update category details |
| `DELETE` | `/api/categories/{id}` | Delete category by ID |

### 2. Questions (`/api/questions`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/questions` | Get all questions |
| `GET` | `/api/questions/{id}` | Get single question by ID |
| `GET` | `/api/questions/category/{categoryId}` | Get active questions for a category |
| `GET` | `/api/questions/search` | Search & filter questions (`?search=Polymorphism&categoryId=1&difficulty=EASY`) |
| `POST` | `/api/questions` | Create a new question |
| `PUT` | `/api/questions/{id}` | Update an existing question |
| `DELETE` | `/api/questions/{id}` | Delete a question |

### 3. Quiz Attempts (`/api/attempts`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/attempts` | Submit a quiz attempt and receive immediate score & feedback |
| `GET` | `/api/attempts/{id}` | Get detailed attempt breakdown by ID |
| `GET` | `/api/attempts/user/{userId}` | Get all quiz attempts taken by a specific user |
| `GET` | `/api/attempts` | List all historical quiz attempts |

---

## 🧪 Sample Request & Response Payloads

### Submitting a Quiz Attempt
```http
POST /api/attempts HTTP/1.1
Host: localhost:8081
Content-Type: application/json

{
  "categoryId": 1,
  "answers": [
    {
      "questionId": 1,
      "selectedAnswer": "Compile-time polymorphism is method overloading; runtime polymorphism is method overriding."
    },
    {
      "questionId": 4,
      "selectedAnswer": "Abstract classes can have state and constructors; interfaces cannot."
    }
  ]
}
```

#### Response (`201 Created`):
```json
{
  "id": 1,
  "userId": null,
  "userName": "Anonymous",
  "categoryId": 1,
  "categoryName": "Java Core and OOP",
  "totalQuestions": 2,
  "correctAnswers": 2,
  "wrongAnswers": 0,
  "score": 100.0,
  "status": "COMPLETED",
  "startedAt": "2026-08-22T19:02:24",
  "completedAt": "2026-08-22T19:02:24",
  "answers": [
    {
      "id": 1,
      "questionId": 1,
      "questionTitle": "What is Polymorphism in Java?",
      "questionText": "Explain compile-time vs runtime polymorphism with examples in Java.",
      "selectedAnswer": "Compile-time polymorphism is method overloading; runtime polymorphism is method overriding.",
      "correctAnswer": "Compile-time polymorphism is achieved via method overloading; runtime polymorphism is achieved via method overriding.",
      "isCorrect": true,
      "explanation": "Compile-time polymorphism is achieved via method overloading; runtime polymorphism is achieved via method overriding."
    }
  ]
}
```

---

## 🛠️ Tech Stack & Dependencies

* **Framework**: Spring Boot 4.0.7 / Spring Framework 7
* **Language**: Java 21 (LTS)
* **ORM**: Hibernate 7.2 / Spring Data JPA
* **Database**: MySQL 8.4
* **Connection Pool**: HikariCP
* **Boilerplate Reduction**: Project Lombok
* **Validation**: Jakarta Bean Validation
* **Build Tool**: Apache Maven

---

## 💻 Getting Started Locally

### Prerequisites
* JDK 21+
* Apache Maven 3.9+
* MySQL 8.0+

### Setup & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Abinash330/JavaReady.git
   cd JavaReady/javaready-backend
   ```
2. Configure database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/defaultdb?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
3. Build and run:
   ```bash
   mvn clean spring-boot:run
   ```
4. Access the API on: `http://localhost:8081`

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
