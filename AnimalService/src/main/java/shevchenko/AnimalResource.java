package shevchenko;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnimalResource {

    @Inject
    AnimalService service;

    @GET
    public List<Animal> getAll() {
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

    @POST
    public Animal createAnimal(Animal animal) {
        return service.createAnimal(animal);
    }

    @PUT
    public Animal updateAnimal(Animal animal) {
        return service.updateAnimal(animal);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAnimal(@PathParam("id") Long id) {
        boolean deleted = service.deleteAnimal(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Цей endpoint викликається з Adoption Service для зміни статусу
    @POST
    @Path("/{id}/adopt")
    public Response markAsAdopted(@PathParam("id") Long id) {
        try {
            Animal animal = service.markAsAdopted(id);
            return Response.ok(animal).build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Animal not found")) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (e.getMessage().equals("Already adopted")) {
                return Response.status(Response.Status.CONFLICT).entity("Already adopted").build();
            }
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/health-status")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateHealthStatus(@PathParam("id") Long id, String status) {
        try {
            Animal animal = service.updateHealthStatus(id, status);
            return Response.ok(animal).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}