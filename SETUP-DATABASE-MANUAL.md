# Manual Database Setup Instructions

If `psql` is not in your PATH, follow these manual steps:

## Option 1: Using pgAdmin (Recommended for Windows)

1. **Open pgAdmin** (usually installed with PostgreSQL)

2. **Connect to PostgreSQL Server**
   - Right-click on "Servers" → "Create" → "Server"
   - Or use existing connection
   - Enter password when prompted (default: `1234567890` if you set it, or your PostgreSQL password)

3. **Create the Database**
   - Right-click on "Databases" → "Create" → "Database"
   - Name: `pethouse`
   - Click "Save"

4. **Create the Schemas**
   - Expand the `pethouse` database
   - Right-click on "Schemas" → "Create" → "Schema"
   - Create three schemas:
     - `animal`
     - `adoption`
     - `health`

5. **Grant Privileges** (Optional - usually not needed for postgres user)
   - Open Query Tool (Right-click on `pethouse` → "Query Tool")
   - Run:
   ```sql
   GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;
   ```

## Option 2: Using psql with Full Path

1. **Find your PostgreSQL installation path**
   - Usually: `C:\Program Files\PostgreSQL\16\bin\` (version may vary)

2. **Open Command Prompt** and run:
   ```cmd
   "C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres
   ```

3. **Run the SQL commands**:
   ```sql
   CREATE DATABASE pethouse;
   \c pethouse
   CREATE SCHEMA IF NOT EXISTS animal;
   CREATE SCHEMA IF NOT EXISTS adoption;
   CREATE SCHEMA IF NOT EXISTS health;
   GRANT ALL PRIVILEGES ON SCHEMA animal TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA adoption TO postgres;
   GRANT ALL PRIVILEGES ON SCHEMA health TO postgres;
   \q
   ```

## Option 3: Add PostgreSQL to PATH

1. **Find PostgreSQL bin directory**
   - Usually: `C:\Program Files\PostgreSQL\16\bin\`

2. **Add to PATH**:
   - Right-click "This PC" → "Properties"
   - "Advanced system settings" → "Environment Variables"
   - Under "System variables", find "Path" → "Edit"
   - "New" → Add: `C:\Program Files\PostgreSQL\16\bin\` (adjust version)
   - Click "OK" on all dialogs

3. **Restart Command Prompt** and run `create-database.bat` again

## Option 4: Use the SQL File Directly

1. **Open pgAdmin** → Connect to PostgreSQL

2. **Open Query Tool** (Right-click on `postgres` database → "Query Tool")

3. **First, create the database**:
   ```sql
   CREATE DATABASE pethouse;
   ```

4. **Then connect to pethouse and run the schema creation**:
   - In Query Tool, change database to `pethouse` (dropdown at top)
   - Or open new Query Tool for `pethouse` database
   - Copy and paste the contents of `database-init.sql` (skip the comments about connecting)

## Verification

After setup, verify the schemas exist:

```sql
\c pethouse
\dn
```

You should see: `animal`, `adoption`, `health`

## Next Steps

Once the database and schemas are created:

1. **Start your Quarkus services** - tables will be created automatically
2. **(Optional) Seed initial data** - run `database-seed-data.sql` after tables are created

