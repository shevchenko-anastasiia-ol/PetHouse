# Fix Token Propagation 404 Error

## Problem
Getting 404 when AdoptionService calls AnimalService's `/animals/{id}/adopt` endpoint, even though the endpoint is registered.

## Root Cause
The token propagation filter might not be working correctly. Changed from reactive to blocking filter.

## Changes Made

1. **Changed token propagation filter** in AdoptionService:
   - From: `io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter`
   - To: `io.quarkus.oidc.token.propagation.AccessTokenRequestFilter`

2. **Added debug logging** to see what's happening

## Next Steps

1. **Restart AdoptionService**:
   ```bash
   cd AdoptionService
   # Stop (Ctrl+C)
   mvn clean quarkus:dev
   ```

2. **Check the logs** when creating an adoption:
   - Should see token propagation messages
   - Should see the request being made to AnimalService
   - Check for any authentication errors

3. **If still getting 404**, check:
   - AnimalService logs - is the request reaching it?
   - AdoptionService logs - is the token being propagated?
   - Check if there are any security filter errors

## Alternative: Try Both Filters

If the blocking filter doesn't work, you can try removing the provider specification and let Quarkus auto-detect:

```properties
quarkus.rest-client.animal-service.token-propagation=true
# Remove the providers line - let Quarkus choose automatically
```

Or try both:
```properties
quarkus.rest-client.animal-service.providers=io.quarkus.oidc.token.propagation.AccessTokenRequestFilter,io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter
```

