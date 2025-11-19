# Database Credentials Configuration Guide

## Current Configuration

Database credentials are configured in each service's `application.properties` file:

- **AnimalService**: `AnimalService/src/main/resources/application.properties`
- **AdoptionService**: `AdoptionService/src/main/resources/application.properties`
- **HealthService**: `HealthService/src/main/resources/application.properties`

## Current Settings

All services use:
- **Username**: `postgres`
- **Password**: `1234567890`
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `pethouse`

## How to Update Credentials

### Option 1: Direct Edit (Development)

Edit the `application.properties` file in each service:

```properties
# Database Configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=YOUR_USERNAME
quarkus.datasource.password=YOUR_PASSWORD
quarkus.datasource.jdbc.url=jdbc:postgresql://YOUR_HOST:5432/pethouse?currentSchema=SCHEMA_NAME
```

**Important**: Update in all three services:
- AnimalService: `currentSchema=animal`
- AdoptionService: `currentSchema=adoption`
- HealthService: `currentSchema=health`

### Option 2: Environment Variables (Recommended for Production)

Use environment variables to avoid hardcoding credentials:

1. **Set environment variables**:
   ```bash
   # Windows (Command Prompt)
   set DB_USERNAME=postgres
   set DB_PASSWORD=your_password
   set DB_HOST=localhost
   set DB_PORT=5432

   # Windows (PowerShell)
   $env:DB_USERNAME="postgres"
   $env:DB_PASSWORD="your_password"
   $env:DB_HOST="localhost"
   $env:DB_PORT="5432"

   # Linux/Mac
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_password
   export DB_HOST=localhost
   export DB_PORT=5432
   ```

2. **Update application.properties** to use environment variables:
   ```properties
   # Database Configuration
   quarkus.datasource.db-kind=postgresql
   quarkus.datasource.username=${DB_USERNAME:postgres}
   quarkus.datasource.password=${DB_PASSWORD:1234567890}
   quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/pethouse?currentSchema=animal
   ```

   The syntax `${VAR_NAME:default_value}` means:
   - Use environment variable `VAR_NAME` if set
   - Otherwise use `default_value`

### Option 3: Profile-Specific Configuration

Create separate configuration files for different environments:

**application-dev.properties** (Development):
```properties
quarkus.datasource.username=postgres
quarkus.datasource.password=1234567890
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/pethouse?currentSchema=animal
```

**application-prod.properties** (Production):
```properties
quarkus.datasource.username=${DB_USERNAME}
quarkus.datasource.password=${DB_PASSWORD}
quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/pethouse?currentSchema=animal
```

Then activate the profile:
```bash
# Development (default)
mvn quarkus:dev

# Production
mvn quarkus:dev -Dquarkus.profile=prod
```

## Connection URL Format

The JDBC URL format is:
```
jdbc:postgresql://[host]:[port]/[database]?currentSchema=[schema]
```

Examples:
- Local: `jdbc:postgresql://localhost:5432/pethouse?currentSchema=animal`
- Remote: `jdbc:postgresql://192.168.1.100:5432/pethouse?currentSchema=animal`
- With SSL: `jdbc:postgresql://localhost:5432/pethouse?currentSchema=animal&ssl=true`

## Testing Connection

After updating credentials, test the connection by starting the service:

```bash
cd AnimalService
mvn quarkus:dev
```

Check the logs for connection errors. If successful, you'll see Hibernate creating/updating tables.

## Troubleshooting

### Connection Refused
- Check PostgreSQL is running: `pg_isready` or check services
- Verify host and port are correct

### Authentication Failed
- Verify username and password
- Check PostgreSQL `pg_hba.conf` allows connections

### Database Does Not Exist
- Create database: `CREATE DATABASE pethouse;`
- Or update URL to point to existing database

### Schema Does Not Exist
- Create schemas: `CREATE SCHEMA animal; CREATE SCHEMA adoption; CREATE SCHEMA health;`
- Or remove `currentSchema` parameter from URL

## Security Notes

⚠️ **Never commit passwords to version control!**

- Use environment variables for production
- Add `application.properties` with passwords to `.gitignore` if needed
- Consider using secrets management tools (Vault, AWS Secrets Manager, etc.) for production

