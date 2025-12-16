# Spring Boot Testcontainers Docker Compose

A demonstration Spring Boot application showcasing integration testing with Testcontainers and Docker Compose.

## Overview

This project demonstrates how to:

- Use Testcontainers with Docker Compose for integration testing
- Manage database schema migrations with Liquibase
- Build a CRUD REST API with Spring Data REST
- Containerize PostgreSQL for consistent test environments

## Technologies

- **Java**: 21 (via Gradle Toolchain)
- **Spring Boot**: 3.5.8
- **Gradle**: 8.14.1 (Groovy DSL)
- **Database**: PostgreSQL 17 Alpine
- **Migration**: Liquibase 4.27
- **Testing**: Testcontainers 1.20.4, JUnit 5
- **ORM**: Spring Data JPA with Hibernate

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/id/my/hendisantika/testcontainers/
│   │   │   ├── SpringbootTestcontainersDockerComposeApplication.java
│   │   │   ├── domain/
│   │   │   │   └── Person.java
│   │   │   └── repository/
│   │   │       └── PersonRepository.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       ├── compose.yml
│       └── java/id/my/hendisantika/testcontainers/
│           ├── AbstractIntegrationTest.java
│           ├── PersonCrudRepoIntegrationTest.java
│           └── SpringbootTestcontainersDockerComposeApplicationTests.java
├── liquibase/
│   ├── changelog/
│   │   └── db.changelog.xml
│   └── scripts/
│       ├── 01_schema.sql
│       ├── 02_person_create_table.sql
│       └── 03_person_add_column.sql
└── build.gradle
```

## Features

### REST API

Auto-generated REST endpoints via Spring Data REST at `/person`:

- `GET /person` - List all persons (with pagination)
- `GET /person/{id}` - Get person by ID
- `POST /person` - Create new person
- `PUT /person/{id}` - Update person
- `DELETE /person/{id}` - Delete person

### Person Entity

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30
}
```

## Prerequisites

- **Java 21** (can be installed via SDKMAN)
- **Docker** and **Docker Compose**
- **Gradle** (wrapper included)

## Getting Started

### Build the Project

```bash
./gradlew clean build
```

This will:

1. Compile the source code
2. Start Docker containers (PostgreSQL + Liquibase) via Testcontainers
3. Run database migrations automatically
4. Execute all integration tests
5. Package the application as a bootable JAR

### Run Tests Only

```bash
./gradlew test
```

### Run the Application

For local development, ensure PostgreSQL is running with:

- Host: `localhost:5432`
- Database: `postgres`
- Schema: `testcontainer`
- Username: `postgres`
- Password: `postgres`

Then run:

```bash
./gradlew bootRun
```

Or run the JAR directly:

```bash
java -jar build/libs/testcontainers-0.0.1-SNAPSHOT.jar
```

## Testing Architecture

### Docker Compose Setup

The test suite uses a Docker Compose configuration (`src/test/compose.yml`) that orchestrates:

1. **PostgreSQL Container** (`db-service-test`)
    - Image: `postgres:17-alpine3.20`
    - Exposes port 5432
    - Configured with resource limits (1 CPU, 250MB memory)

2. **Liquibase Container** (`liquibase-test`)
    - Image: `liquibase/liquibase:4.27`
    - Automatically applies database migrations
    - Waits for PostgreSQL to be ready

### Testcontainers Integration

The `AbstractIntegrationTest` base class:

- Starts Docker Compose containers before all tests
- Waits for Liquibase migrations to complete
- Dynamically configures Spring datasource properties
- Ensures consistent test environment across runs

### Database Migrations

Liquibase manages schema evolution:

1. Creates `testcontainer` schema
2. Creates `person` table with id, name, email
3. Adds `age` column to person table

## Dependencies

### Main Dependencies

- `spring-boot-starter-actuator` - Health checks and metrics
- `spring-boot-starter-data-jpa` - JPA/Hibernate support
- `spring-boot-starter-data-rest` - REST endpoints
- `spring-boot-starter-web` - Web framework
- `postgresql` - PostgreSQL JDBC driver
- `commons-csv` - CSV parsing utilities
- `commons-io` - I/O utilities
- `lombok` - Boilerplate reduction

### Test Dependencies

- `spring-boot-starter-test` - Spring testing support
- `testcontainers` - Container management
- `junit-jupiter` - JUnit 5 integration

## Configuration

### Application Properties (`application.yml`)

```yaml
spring:
  application:
    name: springboot-testcontainers-docker-compose
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres?currentSchema=testcontainer
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

Key configurations:

- `ddl-auto: validate` - Hibernate validates schema against entities
- `currentSchema=testcontainer` - Uses custom PostgreSQL schema

## Notes

- The project was upgraded to use Groovy DSL for better Java 21+ compatibility
- Tests automatically clean up Docker resources after completion
- The application uses schema validation (not generation) for safety

## License

This project is for demonstration purposes.

## Author

- **Hendi Santika**
- Email: hendisantika@gmail.com
- Telegram: @hendisantika34
