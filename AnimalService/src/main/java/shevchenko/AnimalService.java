package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AnimalService {

    public List<Animal> getAll() {
        return Animal.listAll();
    }

    public Optional<Animal> getById(Long id) {
        return Animal.findByIdOptional(id);
    }

    @Transactional
    public Animal createAnimal(Animal animal) {
        // For new entities, ensure ID is null so Panache generates it
        // Always clear ID for create operations - Panache will auto-generate it
        animal.id = null;
        animal.persistAndFlush(); // Force immediate insert to catch any errors
        return animal;
    }

    @Transactional
    public Animal updateAnimal(Animal animal) {
        Animal existing = Animal.findById(animal.id);
        if (existing == null) {
            throw new RuntimeException("Animal not found");
        }
        existing.name = animal.name;
        existing.species = animal.species;
        existing.age = animal.age;
        existing.healthStatus = animal.healthStatus;
        existing.adopted = animal.adopted;
        existing.persist();
        return existing;
    }

    @Transactional
    public boolean deleteAnimal(Long id) {
        return Animal.deleteById(id);
    }

    // Метод для зміни статусу adopted (викликається через gRPC/REST з Adoption Service)
    @Transactional
    public Animal markAsAdopted(Long id) {
        Animal animal = Animal.findById(id);
        if (animal == null) {
            throw new RuntimeException("Animal not found");
        }

        if (animal.adopted) {
            throw new RuntimeException("Already adopted");
        }

        animal.adopted = true;
        animal.persist();
        return animal;
    }

    @Transactional
    public Animal updateHealthStatus(Long id, String status) {
        Animal animal = Animal.findById(id);


        animal.healthStatus = status;
            animal.persist();
            return animal;

    }
}