# Fix Primary Key Constraint Violation

## Problem
PostgreSQL error: `duplicate key value violates unique constraint "animals_pkey"` or `"adoptions_pkey"`. Detail: Key (id)=(1) already exists.

This happens when:
- The database already has data with id=1
- The sequence is not properly synchronized with existing data
- Trying to insert a new record with an ID that already exists

## Solution

### Option 1: Reset All Database Sequences (Recommended)

Run this SQL script to fix all sequences at once:

```sql
psql -U postgres -d pethouse -f fix-all-sequences-quick.sql
```

Or run manually:

```sql
\c pethouse

-- Fix animals sequence
SET search_path TO animal;
SELECT setval('animals_id_seq', COALESCE((SELECT MAX(id) FROM animals), 0) + 1, false);

-- Fix adoptions sequence
SET search_path TO adoption;
SELECT setval('adoptions_id_seq', COALESCE((SELECT MAX(id) FROM adoptions), 0) + 1, false);

-- Fix health_records sequence
SET search_path TO health;
SELECT setval('health_records_id_seq', COALESCE((SELECT MAX(id) FROM health_records), 0) + 1, false);
```

Or use the detailed script:
```bash
psql -U postgres -d pethouse -f fix-database-sequence.sql
```

### Option 2: Clear Existing Data (If you don't need it)

```sql
\c pethouse
SET search_path TO animal;
TRUNCATE TABLE animals RESTART IDENTITY CASCADE;
```

This will:
- Delete all animals
- Reset the sequence to start from 1

### Option 3: Drop and Recreate Tables

If you're in development and don't need the data:

1. Stop AnimalService
2. Delete the tables:
```sql
\c pethouse
SET search_path TO animal;
DROP TABLE IF EXISTS animals CASCADE;
```
3. Restart AnimalService - Hibernate will recreate the tables

## Fix Quarkus Dev Mode Error

The `ArrayIndexOutOfBoundsException` in dev mode is usually caused by:
- Corrupted class files
- Hot reload issues

**Solution:**
1. Stop the service (Ctrl+C)
2. Clean and rebuild:
```bash
mvn clean
mvn quarkus:dev
```

Or if that doesn't work:
```bash
mvn clean
rm -rf target/
mvn quarkus:dev
```

## Quick Fix Script

Run this to fix the sequence issue:

```sql
\c pethouse
SET search_path TO animal;
SELECT setval('animals_id_seq', COALESCE((SELECT MAX(id) FROM animals), 0) + 1, false);
```

Then restart AnimalService and try creating an animal again.

