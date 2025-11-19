#!/bin/bash
# Database creation script for PetHouse
# This script creates the database and schemas

echo "Creating PetHouse database and schemas..."

# Create database
psql -U postgres -c "SELECT 1 FROM pg_database WHERE datname = 'pethouse'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE pethouse"

# Create schemas
psql -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS animal;"
psql -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS adoption;"
psql -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS health;"

# Grant privileges
psql -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;"
psql -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;"
psql -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;"

echo "Database and schemas created successfully!"
echo "Now start your Quarkus applications to create the tables automatically."

