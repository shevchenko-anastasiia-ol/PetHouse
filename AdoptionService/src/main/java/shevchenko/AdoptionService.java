package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdoptionService {
    @RestClient
    AnimalRestClient animalClient;

    public List<Adoption> getAll() {
        return Adoption.listAll();
    }

    public Optional<Adoption> getById(Long id) {
        return Adoption.findByIdOptional(id);
    }

    public Optional<Adoption> getByAnimalId(Long animalId) {
        return Adoption.find("animalId", animalId).firstResultOptional();
    }

    @Transactional
    public Adoption createAdoption(Adoption adoption) {
        adoption.id = null;
        if (adoption.adoptionDate == null) {
            adoption.adoptionDate = LocalDate.now();
        }

        adoption.persistAndFlush();
        return adoption;
    }

    @Transactional
    public Adoption updateAdoption(Adoption adoption) {
        Adoption existing = Adoption.findById(adoption.id);
        if (existing == null) {
            throw new RuntimeException("Adoption record not found");
        }
        existing.animalId = adoption.animalId;
        existing.adopterName = adoption.adopterName;
        existing.adopterContact = adoption.adopterContact;
        existing.adoptionDate = adoption.adoptionDate;
        existing.notes = adoption.notes;
        existing.persist();
        return existing;
    }

    @Transactional
    public boolean deleteAdoption(Long id) {
        return Adoption.deleteById(id);
    }

    @Transactional
    public Adoption adoptAnimal(Adoption adoption) {
        adoption.id = null;
        if (adoption.adoptionDate == null) {
            adoption.adoptionDate = LocalDate.now();
        }
        adoption.persistAndFlush();

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

        return adoption;
    }
}
