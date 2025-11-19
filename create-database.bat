@echo off
REM Database creation script for PetHouse (Windows)
REM This script creates the database and schemas

echo Creating PetHouse database and schemas...
echo.

REM Try to find psql in common PostgreSQL installation paths
set PSQL_PATH=
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin\psql.exe
if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin\psql.exe
if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin\psql.exe
if exist "C:\Program Files\PostgreSQL\13\bin\psql.exe" set PSQL_PATH=C:\Program Files\PostgreSQL\13\bin\psql.exe
if exist "C:\Program Files (x86)\PostgreSQL\16\bin\psql.exe" set PSQL_PATH=C:\Program Files (x86)\PostgreSQL\16\bin\psql.exe
if exist "C:\Program Files (x86)\PostgreSQL\15\bin\psql.exe" set PSQL_PATH=C:\Program Files (x86)\PostgreSQL\15\bin\psql.exe

REM Check if psql is in PATH
where psql >nul 2>&1
if %errorlevel% == 0 (
    set PSQL_CMD=psql
    goto :run_commands
)

REM If found in common paths, use it
if not "%PSQL_PATH%"=="" (
    set PSQL_CMD="%PSQL_PATH%"
    goto :run_commands
)

REM psql not found
echo ERROR: psql command not found!
echo.
echo Please do one of the following:
echo 1. Add PostgreSQL bin directory to your PATH environment variable
echo    (Usually: C:\Program Files\PostgreSQL\XX\bin)
echo.
echo 2. Or run the SQL commands manually using pgAdmin or another PostgreSQL client:
echo    - Connect to PostgreSQL as user 'postgres'
echo    - Run the commands from database-init.sql
echo.
echo 3. Or use the SQL file directly in pgAdmin:
echo    - Right-click on 'postgres' database -^> Query Tool
echo    - Open and run database-init.sql
echo.
echo Manual SQL commands:
echo =====================
echo CREATE DATABASE pethouse;
echo \c pethouse
echo CREATE SCHEMA IF NOT EXISTS animal;
echo CREATE SCHEMA IF NOT EXISTS adoption;
echo CREATE SCHEMA IF NOT EXISTS health;
echo GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;
echo GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;
echo GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;
echo.
pause
exit /b 1

:run_commands
echo Found psql at: %PSQL_CMD%
echo.

REM Create database (if it doesn't exist)
echo Checking if database exists...
%PSQL_CMD% -U postgres -c "SELECT 1 FROM pg_database WHERE datname = 'pethouse'" 2>nul | findstr /C:"1" >nul
if errorlevel 1 (
    echo Creating database pethouse...
    %PSQL_CMD% -U postgres -c "CREATE DATABASE pethouse"
    if errorlevel 1 (
        echo ERROR: Failed to create database. Please check PostgreSQL is running and credentials are correct.
        pause
        exit /b 1
    )
) else (
    echo Database pethouse already exists.
)

REM Create schemas
echo Creating schemas...
%PSQL_CMD% -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS animal;"
%PSQL_CMD% -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS adoption;"
%PSQL_CMD% -U postgres -d pethouse -c "CREATE SCHEMA IF NOT EXISTS health;"

REM Grant privileges
echo Granting privileges...
%PSQL_CMD% -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;"
%PSQL_CMD% -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;"
%PSQL_CMD% -U postgres -d pethouse -c "GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;"

echo.
echo Database and schemas created successfully!
echo Now start your Quarkus applications to create the tables automatically.
echo.
pause

