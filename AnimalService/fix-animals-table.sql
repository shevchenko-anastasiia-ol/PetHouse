-- Fix animals table to work with PanacheEntityBase and @GeneratedValue
-- Run this script: psql -U postgres -d pethouse -f fix-animals-table.sql

SET search_path TO animal;

-- Check if the table exists and has data
DO $$
DECLARE
    max_id BIGINT;
BEGIN
    -- Get the maximum ID if table has data
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM animals;
    
    -- Drop the existing table if it exists (WARNING: This will delete all data!)
    DROP TABLE IF EXISTS animals CASCADE;
    
    -- Recreate the table with proper IDENTITY column
    CREATE TABLE animals (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255),
        species VARCHAR(255),
        age INTEGER,
        healthstatus VARCHAR(255),
        adopted BOOLEAN
    );
    
    -- Reset the sequence to start from max_id + 1 (or 1 if no data)
    IF max_id > 0 THEN
        PERFORM setval('animals_id_seq', max_id + 1, false);
    END IF;
    
    RAISE NOTICE 'Table animals recreated successfully';
END $$;

SET search_path TO public;

