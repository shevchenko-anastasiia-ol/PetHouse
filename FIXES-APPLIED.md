# Database Column Mapping Fixes

## Problem
When trying to add or edit data in the UI, errors occurred because of column name mismatches between Java entity fields (camelCase) and PostgreSQL database columns (snake_case).

## Fixes Applied

### 1. Added `@Column` Annotations to Entity Classes

**Animal Entity** (`AnimalService/src/main/java/shevchenko/Animal.java`):
- `healthStatus` → mapped to `health_status` column

**Adoption Entity** (`AdoptionService/src/main/java/shevchenko/Adoption.java`):
- `animalId` → mapped to `animal_id` column
- `adopterName` → mapped to `adopter_name` column
- `adopterContact` → mapped to `adopter_contact` column
- `adoptionDate` → mapped to `adoption_date` column

**HealthRecord Entity** (`HealthService/src/main/java/shevchenko/HealthRecord.java`):
- `animalId` → mapped to `animal_id` column
- `visitDate` → mapped to `visit_date` column
- `vetName` → mapped to `vet_name` column
- `nextAppointment` → mapped to `next_appointment` column
- `healthStatus` → mapped to `health_status` column

### 2. Fixed Create Methods to Handle IDs Properly

Updated all three service classes to properly handle new entities:
- **AnimalService.createAnimal()**: Now clears ID for new entities and handles updates correctly
- **AdoptionService.createAdoption()**: Now clears ID for new entities
- **HealthRecordService.create()**: Now clears ID for new entities

### 3. Enabled SQL Logging in Development Mode

Added SQL logging to help debug database operations:
```properties
%dev.quarkus.hibernate-orm.log.sql=true
```

## What to Do Next

1. **Restart your Quarkus services** to apply the changes
2. **Check the console logs** - you'll now see SQL queries being executed
3. **Try adding/editing data** in the UI - it should work now!

## If Issues Persist

1. **Check the console logs** for SQL errors - they will show exactly what's wrong
2. **Verify database tables exist** - Hibernate should create them automatically on startup
3. **Check column names in database** - run:
   ```sql
   \d animal.animals
   \d adoption.adoptions
   \d health.health_records
   ```

## Expected Database Schema

After Hibernate creates the tables, you should see:

**animal.animals**:
- `id` (bigint, primary key)
- `name` (varchar)
- `species` (varchar)
- `age` (integer)
- `health_status` (varchar)
- `adopted` (boolean)

**adoption.adoptions**:
- `id` (bigint, primary key)
- `animal_id` (bigint)
- `adopter_name` (varchar)
- `adopter_contact` (varchar)
- `adoption_date` (date)
- `notes` (varchar)

**health.health_records**:
- `id` (bigint, primary key)
- `animal_id` (bigint)
- `visit_date` (date)
- `vet_name` (varchar)
- `diagnosis` (varchar)
- `treatment` (varchar)
- `notes` (varchar)
- `next_appointment` (date)
- `health_status` (varchar)

