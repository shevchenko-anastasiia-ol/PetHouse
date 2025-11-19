-- Quick fix for all sequences - run this in one go
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

SET search_path TO public;

