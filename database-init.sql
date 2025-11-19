-- Database and Schema Initialization Script for PetHouse
-- Run this script as PostgreSQL superuser (postgres) to create the database and schemas
-- Usage: psql -U postgres -f database-init.sql

-- Create database (will fail silently if it already exists)
-- Note: You may need to run this manually: CREATE DATABASE pethouse;

-- The following commands should be run after connecting to pethouse database
-- Connect to the pethouse database first: \c pethouse

-- Create schemas for each service
CREATE SCHEMA IF NOT EXISTS animal;
CREATE SCHEMA IF NOT EXISTS adoption;
CREATE SCHEMA IF NOT EXISTS health;

-- Grant privileges to the postgres user (or your application user)
GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;

-- Grant usage on schemas
GRANT USAGE ON SCHEMA animal TO postgres;
GRANT USAGE ON SCHEMA adoption TO postgres;
GRANT USAGE ON SCHEMA health TO postgres;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA animal GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA adoption GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA health GRANT ALL ON TABLES TO postgres;

