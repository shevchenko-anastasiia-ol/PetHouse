package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdoptionService {
    @Inject
    FakeAdoptionRepository repository;
    @RestClient
    AnimalRestClient animalClient;


    public List<Adoption> getAll() {
        return repository.findAll();
    }

    public Optional<Adoption> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Adoption> getByAnimalId(Long animalId) {
        return repository.findByAnimalId(animalId);
    }

    public Adoption createAdoption(Adoption adoption) {
        return repository.save(adoption);
    }

    public Adoption updateAdoption(Adoption adoption) {
        return repository.update(adoption);
    }

    public boolean deleteAdoption(Long id) {
        return repository.delete(id);
    }


    public Adoption adoptAnimal(Adoption adoption) {
        Adoption saved = repository.save(adoption);

        Response r = animalClient.adoptAnimal(adoption.animalId);
        if (r.getStatus() == 200) {
            System.out.println("Adoption confirmed: " + r.readEntity(String.class));
        } else if (r.getStatus() == 404) {
            throw new RuntimeException("Animal not found");
        } else if (r.getStatus() == 409) {
            throw new RuntimeException("Already adopted");
        } else {
            throw new RuntimeException("Adoption failed: " + r.getStatus());
        }

        return saved;
    }
}
