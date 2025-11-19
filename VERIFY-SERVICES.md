# Verify All Services Are Running

## Quick Check

Make sure all three services are running:

1. **AnimalService** - Port 8081
   - Check: `http://localhost:8081/q/health` (should work without auth)
   - Console should show: "Listening on: http://localhost:8081"

2. **AdoptionService** - Port 8080  
   - Check: `http://localhost:8080/q/health` (should work without auth)
   - Console should show: "Listening on: http://localhost:8080"

3. **HealthService** - Port 8082
   - Check: `http://localhost:8082/q/health` (should work without auth)
   - Console should show: "Listening on: http://localhost:8082"

4. **PetHouseUI** - Port 8083
   - Check: `http://localhost:8083` (should redirect to login)
   - Console should show: "Listening on: http://localhost:8083"

## If AnimalService Shows 404

The endpoint path is now correct: `POST /animals/{id}/adopt`

**Make sure AnimalService is restarted** after the code change:
```bash
cd AnimalService
# Stop it (Ctrl+C if running)
mvn clean quarkus:dev
```

Wait for it to fully start - you should see:
- Database connection successful
- REST endpoints registered
- "Listening on: http://localhost:8081"

## Test the Endpoint

After restarting, check AnimalService logs for:
```
Registered REST endpoint: POST /animals/{id}/adopt
```

If you don't see this, there might be a compilation error. Check the console output.

