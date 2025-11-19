# Database Setup Instructions

This document explains how to set up the PostgreSQL database for the PetHouse application.

## Prerequisites

- PostgreSQL installed and running on localhost:5432
- PostgreSQL superuser access (default user: `postgres`)

## Database Configuration

The application uses:
- **Database name**: `pethouse`
- **Username**: `postgres`
- **Password**: `1234567890`
- **Schemas**: 
  - `animal` (for AnimalService)
  - `adoption` (for AdoptionService)
  - `health` (for HealthService)

## Setup Steps

### Option 1: Using psql Command Line

1. Connect to PostgreSQL as superuser:
   ```bash
   psql -U postgres
   ```

2. Run the initialization script:
   ```bash
   psql -U postgres -f database-init.sql
   ```

3. Start your Quarkus applications (AnimalService, AdoptionService, HealthService)
   - Hibernate will automatically create the tables in each schema

4. (Optional) Seed initial data:
   ```bash
   psql -U postgres -d pethouse -f database-seed-data.sql
   ```

### Option 2: Manual Setup

1. Connect to PostgreSQL:
   ```bash
   psql -U postgres
   ```

2. Create the database:
   ```sql
   CREATE DATABASE pethouse;
   ```

3. Connect to the database:
   ```sql
   \c pethouse
   ```

4. Create the schemas:
   ```sql
   CREATE SCHEMA IF NOT EXISTS animal;
   CREATE SCHEMA IF NOT EXISTS adoption;
   CREATE SCHEMA IF NOT EXISTS health;
   ```

5. Grant privileges:
   ```sql
   GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;
   ```

6. Start your Quarkus applications - tables will be created automatically

## Table Creation

The tables are automatically created by Hibernate when the applications start, based on the entity classes:
- `animal.animals` (from Animal entity)
- `adoption.adoptions` (from Adoption entity)
- `health.health_records` (from HealthRecord entity)

This is configured via `quarkus.hibernate-orm.database.generation=update` in each service's `application.properties`.

## Verification

After starting the services, you can verify the tables were created:

```sql
\c pethouse

-- Check animal schema
\dt animal.*

-- Check adoption schema
\dt adoption.*

-- Check health schema
\dt health.*
```

## Troubleshooting

- **Connection refused**: Ensure PostgreSQL is running on localhost:5432
- **Authentication failed**: Verify username and password in `application.properties`
- **Schema does not exist**: Run the `database-init.sql` script
- **Tables not created**: Check Hibernate logs for errors, ensure schemas exist

