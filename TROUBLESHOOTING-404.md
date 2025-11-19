# Troubleshooting 404 Error for Adoptions

## Error
`Failed to load adoptions: Received: 'Not Found, status code 404' when invoking REST Client method: 'shevchenko.AdoptionServiceClient#getAllAdoptions'`

## Possible Causes

### 1. AdoptionService Not Running
**Check**: Is AdoptionService running on port 8080?

**Solution**: Start AdoptionService
```bash
cd AdoptionService
mvn quarkus:dev
```

### 2. Service Needs Restart After Adding Dependency
**Check**: Did you restart AdoptionService after adding `quarkus-rest-client-oidc-token-propagation`?

**Solution**: 
1. Stop AdoptionService (Ctrl+C)
2. Clean and rebuild:
```bash
cd AdoptionService
mvn clean
mvn quarkus:dev
```

### 3. Endpoint Path Mismatch
**Check**: The endpoint should be at `GET /adoptions`

**Verify**: 
- AdoptionResource has `@Path("/adoptions")` and `@GET` method `getAll()`
- This should map to `GET http://localhost:8080/adoptions`

### 4. Authentication Issue
**Check**: The endpoint requires authentication (`/adoptions/*` requires authenticated policy)

**Solution**: Make sure:
- You're logged in to PetHouseUI
- Token propagation is configured (already done)
- AdoptionService is configured to accept tokens

## Quick Fix Steps

1. **Check if AdoptionService is running**:
   - Look for AdoptionService console/logs
   - Try accessing `http://localhost:8080/q/health` in browser

2. **Restart AdoptionService**:
   ```bash
   cd AdoptionService
   mvn clean quarkus:dev
   ```

3. **Check AdoptionService logs** for:
   - Startup errors
   - Endpoint registration messages
   - Authentication errors

4. **Verify the endpoint**:
   - After AdoptionService starts, check logs for registered endpoints
   - Should see something like: `Registered REST endpoint: /adoptions`

5. **Test directly** (if you have a token):
   ```bash
   # With authentication token
   curl -H "Authorization: Bearer <token>" http://localhost:8080/adoptions
   ```

## Expected Behavior

When AdoptionService starts successfully, you should see in the logs:
- Database connection established
- Hibernate creating/updating tables
- REST endpoints registered
- No authentication errors

If you see errors about missing dependencies or class not found, you need to rebuild:
```bash
mvn clean compile quarkus:dev
```

