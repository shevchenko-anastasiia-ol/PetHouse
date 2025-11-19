-- Fix database sequences for all tables
-- Run this in PostgreSQL if you're getting primary key constraint violations

-- Connect to the database
\c pethouse

-- ============================================
-- Fix animals table sequence
-- ============================================
SET search_path TO animal;

-- Reset sequence to the maximum ID + 1
SELECT setval('animals_id_seq', COALESCE((SELECT MAX(id) FROM animals), 0) + 1, false);

-- Verify
SELECT 'animals_id_seq' as sequence_name, last_value FROM animals_id_seq;

-- ============================================
-- Fix adoptions table sequence
-- ============================================
SET search_path TO adoption;

-- Reset sequence to the maximum ID + 1
SELECT setval('adoptions_id_seq', COALESCE((SELECT MAX(id) FROM adoptions), 0) + 1, false);

-- Verify
SELECT 'adoptions_id_seq' as sequence_name, last_value FROM adoptions_id_seq;

-- ============================================
-- Fix health_records table sequence
-- ============================================
SET search_path TO health;

-- Reset sequence to the maximum ID + 1
SELECT setval('health_records_id_seq', COALESCE((SELECT MAX(id) FROM health_records), 0) + 1, false);

-- Verify
SELECT 'health_records_id_seq' as sequence_name, last_value FROM health_records_id_seq;

-- Reset search path
SET search_path TO public;

