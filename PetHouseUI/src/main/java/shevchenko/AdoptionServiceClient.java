package shevchenko;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "adoption-service")
@Path("/adoptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AdoptionServiceClient {

    @GET
    List<Adoption> getAllAdoptions();

    @GET
    @Path("/{id}")
    Response getById(@PathParam("id") Long id);

    @POST
    Adoption adoptAnimal(Adoption adoption);

    @PUT
    @Path("/{id}")
    Response updateAdoption(@PathParam("id") Long id, Adoption adoption);

    @DELETE
    @Path("/{id}")
    Response deleteAdoption(@PathParam("id") Long id);
}