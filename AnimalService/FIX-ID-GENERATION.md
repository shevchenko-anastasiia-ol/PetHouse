# Fix ID Generation Issue

## Problem
After converting from `PanacheEntity` to `PanacheEntityBase` with repository pattern, the database table structure doesn't match the new entity definition. The `id` column needs to be configured as an IDENTITY column for PostgreSQL.

## Solution Options

### Option 1: Drop and Recreate (Recommended for Dev Mode)
The application.properties has been configured to use `drop-and-create` in dev mode. Simply restart the AnimalService:

```bash
# Stop the service (Ctrl+C)
# Restart it
cd AnimalService
mvn quarkus:dev
```

This will automatically recreate the table with the correct structure. **Note: This will delete all existing data.**

### Option 2: Fix Existing Table (Preserves Data)
If you need to preserve existing data, run this SQL script:

```bash
psql -U postgres -d pethouse -f fix-animals-table-safe.sql
```

Or manually run in psql:

```sql
\c pethouse
SET search_path TO animal;

-- Create sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS animals_id_seq;

-- Set sequence to current max ID
SELECT setval('animals_id_seq', COALESCE((SELECT MAX(id) FROM animals), 1), true);

-- Alter the column to use the sequence
ALTER TABLE animals ALTER COLUMN id DROP NOT NULL;
ALTER TABLE animals ALTER COLUMN id SET DEFAULT nextval('animals_id_seq');
ALTER TABLE animals ALTER COLUMN id SET NOT NULL;
ALTER SEQUENCE animals_id_seq OWNED BY animals.id;
```

### Option 3: Manual Table Recreation (If Option 2 doesn't work)
If the safe fix doesn't work, you can manually recreate the table:

```sql
\c pethouse
SET search_path TO animal;

-- Backup data (optional)
CREATE TABLE animals_backup AS SELECT * FROM animals;

-- Drop and recreate
DROP TABLE animals CASCADE;
CREATE TABLE animals (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    species VARCHAR(255),
    age INTEGER,
    healthstatus VARCHAR(255),
    adopted BOOLEAN
);

-- Restore data (if needed)
-- INSERT INTO animals (name, species, age, healthstatus, adopted)
-- SELECT name, species, age, healthstatus, adopted FROM animals_backup;
```

## After Fixing
Once the table is fixed, restart the AnimalService and the ID generation should work correctly.

