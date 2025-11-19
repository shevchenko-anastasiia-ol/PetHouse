# Fix 401 Unauthorized Error

## Problem
Getting 401 Unauthorized when AdoptionService calls AnimalService, even though token propagation is enabled.

## Root Cause
Token propagation requires:
1. The incoming request to AdoptionService must have a valid JWT token
2. The token must be in the security context when the REST client is called
3. The token must be propagated to AnimalService

## Solution

The configuration looks correct. The issue might be:

1. **Token not in security context**: When PetHouseUI calls AdoptionService, the token should be in the Authorization header and automatically available in the security context.

2. **REST client called outside request context**: Make sure the REST client is called synchronously within the same request thread.

## Verify Token Propagation

Check AdoptionService logs when creating an adoption. You should see:
- OIDC token validation messages
- REST client request being made with Authorization header
- Token propagation filter being invoked

## Debug Steps

1. **Check PetHouseUI is sending token**:
   - When PetHouseUI calls AdoptionService, it should include the token
   - Check PetHouseUI logs for the request

2. **Check AdoptionService receives token**:
   - Look for OIDC validation messages in AdoptionService logs
   - Should see token being validated successfully

3. **Check token propagation**:
   - Look for REST client debug messages
   - Should see Authorization header being added to the request

4. **Check AnimalService receives token**:
   - Look for OIDC validation messages in AnimalService logs
   - Should see token being validated

## Alternative: Add Explicit Token Injection

If automatic propagation doesn't work, you can manually inject and pass the token:

```java
@Inject
SecurityIdentity securityIdentity;

@Inject
@RestClient
AnimalRestClient animalClient;

public Adoption adoptAnimal(Adoption adoption) {
    // ... existing code ...
    
    // Get token from security context
    String token = securityIdentity.getCredential(AccessTokenCredential.class).getToken();
    
    // Call with explicit token (if REST client supports it)
    Response r = animalClient.adoptAnimal(adoption.animalId);
    // ...
}
```

But this should not be necessary if token propagation is configured correctly.

