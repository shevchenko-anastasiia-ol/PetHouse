package shevchenko;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "animal-service")
@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AnimalServiceClient {

    @GET
    List<Animal> getAllAnimals();

    @GET
    @Path("/{id}")
    Animal getById(@PathParam("id") Long id);

    @POST
    Animal createAnimal(Animal animal);

    @PUT
    Animal updateAnimal(Animal animal);

    @POST
    @Path("/{id}/adopt")
    Response adoptAnimal(@PathParam("id") Long id);

    @PATCH
    @Path("/{id}/health-status")
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateHealthStatus(@PathParam("id") Long id, String status);
}