package shevchenko;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/adoptions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdoptionResource {
    private static final Logger Log = Logger.getLogger(AdoptionResource.class);
    
    @Inject
    AdoptionService service;

    @POST
    public Adoption adopt(Adoption adoption) {
        return service.adoptAnimal(adoption);
    }

    @GET
    public List<Adoption> getAll() {
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

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Adoption adoption) {
        try {
            adoption.id = id;
            Adoption updated = service.updateAdoption(adoption);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean removed = service.deleteAdoption(id);
        if (removed) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/start/{adopterName}/{animalId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Adoption start(@PathParam("adopterName") String adopterName,
                         @PathParam("animalId") Long animalId) {
        Log.infof("Starting adoption for %s with animal %s", adopterName, animalId);
        return service.start(adopterName, animalId);
    }

    @Path("/end/{adopterName}/{animalId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Adoption end(@PathParam("adopterName") String adopterName,
                       @PathParam("animalId") Long animalId) {
        Log.infof("Ending adoption for %s with animal %s", adopterName, animalId);
        return service.end(adopterName, animalId);
    }
}