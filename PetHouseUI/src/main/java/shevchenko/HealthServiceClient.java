package shevchenko;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "health-service")
@Path("/health-records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface HealthServiceClient {

    @GET
    List<HealthRecord> getAllHealthRecords();

    @GET
    @Path("/{id}")
    Response getById(@PathParam("id") Long id);

    @GET
    @Path("/animal/{animalId}")
    List<HealthRecord> getByAnimalId(@PathParam("animalId") Long animalId);

    @GET
    @Path("/animal/{animalId}/latest")
    Response getLatestByAnimalId(@PathParam("animalId") Long animalId);

    @POST
    HealthRecord createHealthRecord(HealthRecord record);

    @PUT
    @Path("/{id}")
    Response updateHealthRecord(@PathParam("id") Long id, HealthRecord record);

    @DELETE
    @Path("/{id}")
    Response deleteHealthRecord(@PathParam("id") Long id);
}