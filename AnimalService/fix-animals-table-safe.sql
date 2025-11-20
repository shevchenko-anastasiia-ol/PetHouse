-- Safe fix for animals table to work with PanacheEntityBase
-- This script preserves existing data and fixes the ID column
-- Run: psql -U postgres -d pethouse -f fix-animals-table-safe.sql

SET search_path TO animal;

-- Step 1: Create a sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS animals_id_seq;

-- Step 2: Get the current max ID and set the sequence
DO $$
DECLARE
    max_id BIGINT;
BEGIN
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM animals;
    PERFORM setval('animals_id_seq', GREATEST(max_id, 1), true);
END $$;

-- Step 3: Alter the id column to use the sequence as default
-- First, make sure the column allows NULL temporarily
ALTER TABLE animals ALTER COLUMN id DROP NOT NULL;

-- Set the default value to use the sequence
ALTER TABLE animals ALTER COLUMN id SET DEFAULT nextval('animals_id_seq');

-- Make it NOT NULL again
ALTER TABLE animals ALTER COLUMN id SET NOT NULL;

-- Step 4: Make the sequence owned by the column (for IDENTITY behavior)
ALTER SEQUENCE animals_id_seq OWNED BY animals.id;

-- Success message
DO $$
BEGIN
    RAISE NOTICE 'Table animals fixed successfully - ID column now uses sequence';
END $$;

SET search_path TO public;

