package shevchenko;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Provider
@RequestScoped
public class TokenPropagationFilter implements ClientRequestFilter {

    @Inject
    HttpHeaders httpHeaders;

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ClientRequestContext requestContext) {
        // Try to get token from incoming request headers
        if (httpHeaders != null) {
            String authHeader = httpHeaders.getHeaderString("Authorization");
            if (authHeader != null && !authHeader.isEmpty()) {
                requestContext.getHeaders().add("Authorization", authHeader);
                return;
            }
        }
        
        // Fallback: try to get token from JWT
        if (jwt != null) {
            try {
                String rawToken = jwt.getRawToken();
                if (rawToken != null && !rawToken.isEmpty()) {
                    requestContext.getHeaders().add("Authorization", "Bearer " + rawToken);
                }
            } catch (Exception e) {
                // Ignore if we can't get the token
            }
        }
    }
}

