## Spring Nubbin™ Exam Project Manager
Project developed using Spring Boot, this application is designed to manage projects, tasks, companies, and users. It leverages the Model-View-Controller (MVC) architecture, Spring Security for authentication and authorization, and Spring Data JPA for data operations. The application allows users to create and manage projects, assign tasks, collaborate with other users, and manage company-related data within the system.

---

## 🛠️ Technologies Used

- **Spring Boot** – Backend framework
- **Spring Data JPA** – ORM and repository support
- **Spring Security** – Authentication and authorization
- **Thymeleaf** – Server-side templating engine
- **Maven** – Dependency management and build tool
- **MySQL** – Relational database

---

## 🌱 Seeder & Data Generation

A seeder class is included to pre-populate the database with fake data (companies, users, etc.) for testing and development purposes.

---

## 📦 Entities

- **User**: Used by Spring Security for authentication. Contains username, password, a list of roles, and a reference to a single `Person`
- **Role**: Defines system roles (`ADMIN`, `USER`, etc.)
- **Person**: Basic user profile info (name, etc.)
- **Company**: Represents an organization with users
- **Client**: Represents clients with a subscription plan
- **Project**: Contains one or more tasks and belongs to a company or person
- **Task**: Assignable work items with progress tracking
- **Message**: Comments/messages related to a task (chat-style)

---

## 🔐 Security

The application uses Spring Security with role-based access control. Roles are stored in the database and assigned per user.

---

## 📡 API Endpoints

RESTful API controllers are provided for external systems to interact with projects, tasks, users, and other entities.

The API supports full **Create, Read, Update, and Delete (CRUD)** operations for all core entities.

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or later
- Maven
- MySQL running and accessible

### Environment Variables

You can define these in an `.env` file or export them:
```env
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```
---

## 📚 Main Dependencies

Grouped for clarity (see `pom.xml` for full list):

- **Spring Boot Starters**:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-security`
  - `spring-boot-starter-validation`
  - `spring-boot-starter-thymeleaf`
- **Database**:
  - `mysql-connector-j`
- **Development Tools**:
  - `spring-boot-devtools`
  - `spring-boot-starter-test`, `spring-security-test`
- **UI/Frontend** (via WebJars):
  - `bootstrap`, `bootstrap-icons`
  - `dayjs`, `flatpickr`
- **Others**:
  - `lombok`
  - `dotenv-java`
  - `thymeleaf-extras-springsecurity6`

---

## ▶️ Running the Application

```bash
# Clone the repository
git clone https://github.com/nelvison-benedetto/spring-exam-project-manager
cd spring-nubbin-project-manager

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

![Reference1](./readmefiles/nubbintech1.png)
![Reference2](./readmefiles/nubbintech2.png)
![Referenc3](./readmefiles/nubbintech3.png)
![Referenc4](./readmefiles/nubbintech3.2.png)
![Referenc5](./readmefiles/nubbintech4.png)
![Referenc6](./readmefiles/nubbintech5.png)
![Referenc7](./readmefiles/nubbintech6.png)
![Referenc8](./readmefiles/nubbintech7.png)
![Referenc9](./readmefiles/nubbintech8.png)
![Referenc10](./readmefiles/nubbintech9.png)
![Referenc11](./readmefiles/nubbintech10.png)
![Referenc12](./readmefiles/nubbintech11.png)
![Referenc13](./readmefiles/nubbintech12.png)
![Referenc14](./readmefiles/nubbintech13.png)
![Referenc15](./readmefiles/nubbintech14.png)
![Referenc16](./readmefiles/nubbintech15.png)
![Referenc17](./readmefiles/nubbintech16.png)
![Referenc18](./readmefiles/nubbintech17.png)
![Referenc19](./readmefiles/nubbintech18.png)
![Referenc20](./readmefiles/nubbintech19.png)
![Referenc21](./readmefiles/nubbintech20.2.png)
![Referenc22](./readmefiles/nubbintech21.2.png)
![Referenc23](./readmefiles/nubbintech22.png)
![Referenc23](./readmefiles/nubbintech23.2.png)
![Referenc23](./readmefiles/nubbintech24.png)
![Referenc23](./readmefiles/nubbintech25.2.png)
![Referenc23](./readmefiles/nubbintech26.png)
![Referenc23](./readmefiles/nubbintech27.png)
![Referenc23](./readmefiles/nubbintech28.png)
![Referenc23](./readmefiles/nubbintech29.png)
