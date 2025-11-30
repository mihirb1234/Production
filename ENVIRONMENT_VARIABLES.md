# Environment Variables Configuration

This document describes the environment variables used by the Memes Commerce application.

## Required Environment Variables

### Database Configuration
```bash
DATABASE_URL=jdbc:mysql://db:3306/memesdb
DATABASE_USERNAME=memes_user
DATABASE_PASSWORD=your_secure_password_here
```

### MySQL Configuration (for docker-compose)
```bash
MYSQL_ROOT_PASSWORD=your_root_password_here
```

### Application Configuration
```bash
SERVER_PORT=8080
SHOW_SQL=false
```

### Database Connection Pool Settings
```bash
DB_MAX_POOL_SIZE=10
DB_MIN_IDLE=5
DB_CONNECTION_TIMEOUT=20000
DB_IDLE_TIMEOUT=300000
DB_MAX_LIFETIME=1200000
```

## Setup Instructions

1. Create a `.env` file in the project root (copy from docker-compose.yml values or create your own)
2. Replace placeholder values with your actual secure credentials
3. **Never commit `.env` file to version control**
4. Use strong, unique passwords in production environments

## Security Best Practices

- Use different passwords for different environments
- Store secrets in environment variables, not in code
- Use a secrets management system in production (AWS Secrets Manager, HashiCorp Vault, etc.)
- Rotate passwords regularly
- Limit database user privileges to minimum required

## Docker Usage

When using docker-compose, the environment variables are set in the `docker-compose.yml` file. For production deployments, consider using Docker secrets or external secret management.
