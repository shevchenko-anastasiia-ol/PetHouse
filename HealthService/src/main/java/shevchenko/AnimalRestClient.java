package shevchenko;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/animals")
@RegisterRestClient(baseUri = "http://localhost:8081")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AnimalRestClient {
    @GET
    @Path("/{id}")
    Animal getById(@PathParam("id") Long id);

    @PATCH
    @Path("/{id}/health-status")
    @Consumes(MediaType.TEXT_PLAIN)
    Animal updateHealthStatus(@PathParam("id") Long id, String status);
}
