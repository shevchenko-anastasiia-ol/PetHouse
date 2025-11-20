package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AnimalService {

    @Inject
    AnimalRepository animalRepository;

    public List<Animal> getAll() {
        return animalRepository.listAll();
    }

    public Optional<Animal> getById(Long id) {
        return animalRepository.findByIdOptional(id);
    }

    @Transactional
    public Animal createAnimal(Animal animal) {
        animal.id = null;
        animalRepository.persistAndFlush(animal);
        return animal;
    }

    @Transactional
    public Animal updateAnimal(Animal animal) {
        Animal existing = animalRepository.findById(animal.id);
        if (existing == null) {
            throw new RuntimeException("Animal not found");
        }
        existing.name = animal.name;
        existing.species = animal.species;
        existing.age = animal.age;
        existing.healthStatus = animal.healthStatus;
        existing.adopted = animal.adopted;
        animalRepository.persist(existing);
        return existing;
    }

    @Transactional
    public boolean deleteAnimal(Long id) {
        return animalRepository.deleteById(id);
    }

    @Transactional
    public Animal markAsAdopted(Long id) {
        Animal animal = animalRepository.findById(id);
        if (animal == null) {
            throw new RuntimeException("Animal not found");
        }

        if (animal.adopted) {
            throw new RuntimeException("Already adopted");
        }

        animal.adopted = true;
        animalRepository.persist(animal);
        return animal;
    }

    @Transactional
    public Animal updateHealthStatus(Long id, String status) {
        Animal animal = animalRepository.findById(id);
        if (animal == null) {
            throw new RuntimeException("Animal not found");
        }
        animal.healthStatus = status;
        animalRepository.persist(animal);
        return animal;
    }
}