# How to Verify Endpoints Are Working

## Check if AnimalService is Running

1. **Check the console** - Is AnimalService running on port 8081?
2. **Test the health endpoint** (no auth required):
   ```
   http://localhost:8081/q/health
   ```
   Should return 200 OK

3. **Check registered endpoints** in AnimalService logs:
   - Look for messages like "Registered REST endpoint"
   - Should see `/animals/{id}/adopt` listed

## Test the Adopt Endpoint Directly

If you have a JWT token, test it:
```bash
curl -X POST http://localhost:8081/animals/1/adopt \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json"
```

## Common Issues

### 1. AnimalService Not Running
**Solution**: Start it
```bash
cd AnimalService
mvn quarkus:dev
```

### 2. Endpoint Not Registered After Code Change
**Solution**: Restart AnimalService
```bash
# Stop (Ctrl+C)
cd AnimalService
mvn clean quarkus:dev
```

### 3. Authentication Token Not Propagated
**Check**: AdoptionService logs should show token propagation
**Solution**: Make sure token propagation is configured (already done)

### 4. Path Mismatch
**Current paths**:
- AnimalResource: `POST /animals/{id}/adopt` ✅
- AnimalRestClient: `POST /animals/{id}/adopt` ✅
- These match!

## Debug Steps

1. **Check AdoptionService logs** when creating adoption:
   - Should see the request being made
   - Check for any error messages

2. **Check AnimalService logs**:
   - Should see the incoming request
   - Check if it reaches the endpoint

3. **Verify both services are running**:
   - AdoptionService on port 8080
   - AnimalService on port 8081

4. **Test with a simple GET first**:
   - Try `GET /animals` from AdoptionService
   - If that works, token propagation is working
   - If that fails, it's a token propagation issue

