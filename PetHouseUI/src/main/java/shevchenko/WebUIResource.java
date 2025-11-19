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
                    .data("API_URL", "/api/health-records")
                    .render();
        } catch (Exception e) {
            return healthRecords
                    .data("records", Collections.emptyList())
                    .data("error", "Failed to load health records: " + e.getMessage())
                    .data("message", "")
                    .data("username", getUsername())
                    .data("API_URL", "/api/health-records")
                    .render();
        }
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String logout() {
        return "<html><body><h1>Logged out successfully</h1><a href='/'>Go to Home</a></body></html>";
    }

    // Proxy endpoints for AnimalService
    @POST
    @Path("/api/animals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response createAnimal(Animal animal) {
        try {
            Animal created = animalClient.createAnimal(animal);
            return jakarta.ws.rs.core.Response.ok(created).build();
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating animal: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/api/animals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response updateAnimal(Animal animal) {
        try {
            Animal updated = animalClient.updateAnimal(animal);
            return jakarta.ws.rs.core.Response.ok(updated).build();
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating animal: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/api/animals/{id}")
    @Authenticated
    public jakarta.ws.rs.core.Response deleteAnimal(@PathParam("id") Long id) {
        jakarta.ws.rs.core.Response response = animalClient.deleteAnimal(id);
        return response;
    }

    @POST
    @Path("/api/animals/{id}/adopt")
    @Authenticated
    public jakarta.ws.rs.core.Response adoptAnimal(@PathParam("id") Long id) {
        try {
            jakarta.ws.rs.core.Response response = animalClient.adoptAnimal(id);
            return response;
        } catch (Exception e) {
            return jakarta.ws.rs.core.Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Proxy endpoints for AdoptionService
    @POST
    @Path("/api/adoptions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response createAdoption(Adoption adoption) {
        try {
            Adoption created = adoptionClient.adoptAnimal(adoption);
            return jakarta.ws.rs.core.Response.ok(created).build();
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating adoption: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/api/adoptions/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response updateAdoption(@PathParam("id") Long id, Adoption adoption) {
        try {
            jakarta.ws.rs.core.Response response = adoptionClient.updateAdoption(id, adoption);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating adoption: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/api/adoptions/{id}")
    @Authenticated
    public jakarta.ws.rs.core.Response deleteAdoption(@PathParam("id") Long id) {
        jakarta.ws.rs.core.Response response = adoptionClient.deleteAdoption(id);
        return response;
    }

    // Proxy endpoints for HealthService
    @POST
    @Path("/api/health-records")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response createHealthRecord(HealthRecord record) {
        try {
            HealthRecord created = healthClient.createHealthRecord(record);
            return jakarta.ws.rs.core.Response.ok(created).build();
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating health record: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/api/health-records/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public jakarta.ws.rs.core.Response updateHealthRecord(@PathParam("id") Long id, HealthRecord record) {
        try {
            jakarta.ws.rs.core.Response response = healthClient.updateHealthRecord(id, record);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating health record: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/api/health-records/{id}")
    @Authenticated
    public jakarta.ws.rs.core.Response deleteHealthRecord(@PathParam("id") Long id) {
        try {
            jakarta.ws.rs.core.Response response = healthClient.deleteHealthRecord(id);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting health record: " + e.getMessage()).build();
        }
    }
}