package shevchenko;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "animal-service")
@Path("/animals")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AnimalRestClient {

    @GET
    List<Animal> getAllAnimals();

    @POST
    @Path("/{id}/adopt")
    Response adoptAnimal(@PathParam("id") Long id);
}