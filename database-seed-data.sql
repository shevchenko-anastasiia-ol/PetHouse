-- Seed Data Script for PetHouse Database
-- This script inserts initial data into the tables
-- Run this AFTER the application has started and created the tables

-- Connect to the pethouse database
\c pethouse

-- Set schema context
SET search_path TO animal;

-- Insert initial animals (only if tables are empty)
INSERT INTO animal.animals (id, name, species, age, health_status, adopted)
SELECT 1, 'Bella', 'Dog', 3, 'Healthy', false
WHERE NOT EXISTS (SELECT 1 FROM animal.animals WHERE id = 1);

INSERT INTO animal.animals (id, name, species, age, health_status, adopted)
SELECT 2, 'Milo', 'Cat', 2, 'Needs Vaccination', false
WHERE NOT EXISTS (SELECT 1 FROM animal.animals WHERE id = 2);

INSERT INTO animal.animals (id, name, species, age, health_status, adopted)
SELECT 3, 'Lucy', 'Dog', 1, 'Healthy', true
WHERE NOT EXISTS (SELECT 1 FROM animal.animals WHERE id = 3);

-- Set schema context for adoption
SET search_path TO adoption;

-- Insert initial adoption record
INSERT INTO adoption.adoptions (id, animal_id, adopter_name, adopter_contact, adoption_date, notes)
SELECT 1, 3, 'Olena Ivanova', '+380671234567', '2024-01-10', 'Family with children'
WHERE NOT EXISTS (SELECT 1 FROM adoption.adoptions WHERE id = 1);

-- Set schema context for health
SET search_path TO health;

-- Insert initial health records
INSERT INTO health.health_records (id, animal_id, visit_date, vet_name, diagnosis, treatment, notes, next_appointment, health_status)
SELECT 1, 1, '2024-06-01', 'Dr. Petrenko', 'Checkup', 'Vaccination', 'All good', '2025-06-01', 'Taking treatment'
WHERE NOT EXISTS (SELECT 1 FROM health.health_records WHERE id = 1);

INSERT INTO health.health_records (id, animal_id, visit_date, vet_name, diagnosis, treatment, notes, next_appointment, health_status)
SELECT 2, 2, '2024-07-10', 'Dr. Bondarenko', 'Ear infection', 'Antibiotics', 'Control in 10 days', NULL, 'Need vaccination'
WHERE NOT EXISTS (SELECT 1 FROM health.health_records WHERE id = 2);

-- Reset search path
SET search_path TO public;

