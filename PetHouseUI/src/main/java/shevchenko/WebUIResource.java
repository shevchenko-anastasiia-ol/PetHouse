package shevchenko;

import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Collections;

@Path("/")
public class WebUIResource {

    @Inject
    Template index;

    @Inject
    Template animals;

    @Inject
    Template adoptions;

    @Inject
    Template healthRecords;

    @Inject
    @RestClient
    AnimalServiceClient animalClient;

    @Inject
    @RestClient
    AdoptionServiceClient adoptionClient;

    @Inject
    @RestClient
    HealthServiceClient healthClient;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    @IdToken
    JsonWebToken idToken;

    private String getUsername() {
        if (idToken != null && idToken.getName() != null) {
            return idToken.getName();
        }
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            return securityIdentity.getPrincipal().getName();
        }
        return "Guest";
    }

    private boolean isAuthenticated() {
        return securityIdentity != null && !securityIdentity.isAnonymous();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        return index
                .data("title", "Animal Shelter Management System")
                .data("username", getUsername())
                .data("authenticated", isAuthenticated())
                .render();
    }

    @GET
    @Path("/animals")
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public String getAnimals() {
        try {
            List<Animal> animalList = animalClient.getAllAnimals();
            return animals
                    .data("animals", animalList)
                    .data("error", null)
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8081/animals")
                    .render();
        } catch (Exception e) {
            return animals
                    .data("animals", Collections.emptyList())
                    .data("error", "Failed to load animals: " + e.getMessage())
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8081/animals")
                    .render();
        }
    }

    @GET
    @Path("/adoptions")
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public String getAdoptions() {
        try {
            List<Adoption> adoptionList = adoptionClient.getAllAdoptions();
            return adoptions
                    .data("adoptions", adoptionList)
                    .data("error", null)
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8080/adoptions")
                    .render();
        } catch (Exception e) {
            return adoptions
                    .data("adoptions", Collections.emptyList())
                    .data("error", "Failed to load adoptions: " + e.getMessage())
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8080/adoptions")
                    .render();
        }
    }

    @GET
    @Path("/health-records")
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public String getHealthRecords() {
        try {
            List<HealthRecord> records = healthClient.getAllHealthRecords();
            return healthRecords
                    .data("records", records)
                    .data("error", null)
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8082/health-records")
                    .render();
        } catch (Exception e) {
            return healthRecords
                    .data("records", Collections.emptyList())
                    .data("error", "Failed to load health records: " + e.getMessage())
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "http://localhost:8082/health-records")
                    .render();
        }
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String logout() {
        return "<html><body><h1>Logged out successfully</h1><a href='/'>Go to Home</a></body></html>";
    }
}