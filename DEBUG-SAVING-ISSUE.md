# Debugging Guide for Saving Issue

## What I Fixed

1. **Better Error Messages**: The JavaScript now shows the actual error message from the server
2. **Exception Handling**: Added try-catch blocks in proxy endpoints to catch and return errors
3. **ID Handling**: Fixed JavaScript to not send `id` field when creating new animals

## How to Debug

### 1. Check Browser Console
Open browser DevTools (F12) → Console tab
- Look for error messages when you click "Save"
- Check the "Error response:" log to see what the server returned

### 2. Check PetHouseUI Logs
Look at the PetHouseUI console output:
- Should see stack traces if there are exceptions
- Check for token propagation errors

### 3. Check AnimalService Logs
Look at the AnimalService console output:
- Check for authentication errors
- Check for database errors (SQL errors)
- Check for Hibernate errors

### 4. Common Issues

#### Issue: "401 Unauthorized"
- **Cause**: Token not being propagated correctly
- **Fix**: Make sure you're logged in to PetHouseUI via Keycloak
- **Check**: PetHouseUI logs for token propagation errors

#### Issue: "500 Internal Server Error"
- **Cause**: Database error or entity mapping issue
- **Check**: AnimalService logs for the actual error
- **Common causes**:
  - Column name mismatch (check `healthstatus` vs `health_status`)
  - Database connection issue
  - Transaction rollback

#### Issue: "Failed to fetch"
- **Cause**: Network/CORS issue
- **Fix**: Make sure PetHouseUI is running on port 8083
- **Check**: Browser Network tab to see if request is being sent

## Test Steps

1. **Open Browser DevTools** (F12)
2. **Go to Console tab**
3. **Try to save an animal**
4. **Check the console output** - you should see:
   - Either success message
   - Or detailed error message

5. **Check Network tab**:
   - Find the request to `/api/animals`
   - Check the Response tab to see what the server returned
   - Check the Status code (200 = success, 4xx/5xx = error)

## Expected Behavior

### Creating New Animal:
- Request: `POST /api/animals`
- Body: `{name: "...", species: "...", age: ..., healthStatus: "...", adopted: false}` (NO id field)
- Response: `200 OK` with the created animal (with generated id)

### Updating Animal:
- Request: `PUT /api/animals`
- Body: `{id: 1, name: "...", ...}` (WITH id field)
- Response: `200 OK` with the updated animal

## If Still Not Working

1. **Share the error message** from browser console
2. **Share the logs** from PetHouseUI and AnimalService
3. **Check if you're logged in** - try refreshing the page and logging in again

