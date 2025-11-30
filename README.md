# Memes Commerce Application

A Spring Boot application for memes commerce with MVC architecture and layered design.

## Architecture

- **Framework**: Spring Boot 3.5.0 with Java 17
- **Database**: MySQL (primary), SQLite (fallback), H2 (testing)
- **Architecture**: MVC with layered architecture (Controller → Service → Repository → Model)

## Quick Start with Docker

### Prerequisites
- Docker and Docker Compose installed
- Git

### Setup and Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd memes-commercee
   ```

2. **Build and start services**
   ```bash
   # Using the provided script
   ./docker-run.sh build
   ./docker-run.sh start

   # Or using docker-compose directly
   docker-compose up --build -d
   ```

3. **Check service status**
   ```bash
   ./docker-run.sh status
   # or
   docker-compose ps
   ```

4. **View logs**
   ```bash
   ./docker-run.sh logs
   # or
   docker-compose logs -f
   ```

The application will be available at `http://localhost:8080`

### Stopping Services
```bash
./docker-run.sh stop
# or
docker-compose down
```

### Cleaning Up
```bash
./docker-run.sh clean  # Removes containers, volumes, and prunes system
```

## Configuration

### Environment Variables

Database credentials and other configuration are managed through environment variables. See [ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md) for details.

### Development Override

For local development, you can create a `docker-compose.override.yml` file (copy from `docker-compose.override.yml.example`) to:
- Enable SQL logging
- Mount log directories
- Expose additional ports

## API Endpoints

- Health check: `GET /health_internal`
- User APIs: `/api/users/*`
- Order APIs: `/api/orders/*`
- **Main API**: `GET /api/orders/user/{userId}` - Lists all orders for a user

## Testing

### Running Tests Locally
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=MemesCommerceeApplicationTests

# Run with coverage
./mvnw test jacoco:report
```

### Test Configuration
- **Database**: H2 in-memory database for fast, isolated testing
- **Profile**: `test` profile automatically applied
- **Data**: Test data is created and cleaned up automatically

### API Testing with Postman
1. Import `MemesCommerce.postman_collection.json`
2. Set base URL to `http://localhost:8080`
3. Test the main API: `GET /api/orders/user/1`

## Project Structure

```
src/main/java/com/example/memes_commercee/
├── controller/     # REST API endpoints
├── model/          # JPA entities (User, Order, OrderStatus)
├── repository/     # Data access layer (JPA repositories)
├── service/        # Business logic layer
└── MemesCommerceeApplication.java

src/test/java/com/example/memes_commercee/
└── MemesCommerceeApplicationTests.java  # Integration tests

src/main/resources/
└── application.properties  # Production config

src/test/resources/
└── application.properties  # Test config (H2 database)
```

## Development (without Docker)

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL 8.0

### Setup
1. Install dependencies: `mvn clean install`
2. Set up MySQL database
3. Configure environment variables
4. Run: `mvn spring-boot:run`

## Security

- Database credentials are externalized to environment variables
- Application runs as non-root user in Docker
- Sensitive configuration is not committed to version control

## Monitoring

The Docker setup includes:
- Health checks for both app and database
- Proper restart policies
- Resource limits and monitoring capabilities
