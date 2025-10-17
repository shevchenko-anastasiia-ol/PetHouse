package shevchenko;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnimalResource {

    private final FakeAnimalRepository repository = new FakeAnimalRepository();

    @GET
    public List<Animal> getAll() {
        return repository.findAll();
    }

    @GET
    @Path("/{id}")
    public Animal getById(@PathParam("id") Long id) {
        return repository.findById(id).orElseThrow(() -> new WebApplicationException("Animal not found", 404));
    }

    @POST
    public Animal createAnimal(Animal animal) {
        return repository.save(animal);
    }

    @PUT
    public Animal updateAnimal(Animal animal) {
        return repository.update(animal);
    }

    @POST
    @Path("/{id}/adopt")
    public Response adoptAnimal(@PathParam("id") Long id) {
        var opt = repository.findById(id);
        if (opt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var animal = opt.get();
        if (animal.adopted) {
            return Response.status(Response.Status.CONFLICT).entity("Already adopted").build();
        }
        animal.adopted = true;
        repository.update(animal); // оновлюємо record у фейковому репозиторії
        return Response.ok(animal).build();
    }

    @PATCH
    @Path("/{id}/health-status")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateHealthStatus(@PathParam("id") Long id, String status) {
        try {
            return Response.ok(repository.updateHealthStatus(id, status)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}

