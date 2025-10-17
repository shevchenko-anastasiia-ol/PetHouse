package shevchenko;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/health-records")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HealthRecordResource {

    @Inject
    HealthRecordService service;

    @POST
    public HealthRecord create(HealthRecord record) {
        return service.create(record);
    }

    @GET
    public List<HealthRecord> getAll() {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return service.getById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/animal/{animalId}")
    public List<HealthRecord> getByAnimalId(@PathParam("animalId") Long animalId) {
        return service.getByAnimalId(animalId);
    }

    @GET
    @Path("/animal/{animalId}/latest")
    public Response getLatestByAnimalId(@PathParam("animalId") Long animalId) {
        return service.getLatestByAnimalId(animalId)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, HealthRecord record) {
        try {
            record.id = id;
            return Response.ok(service.update(record)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean removed = service.delete(id);
        return removed ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
