package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FakeAnimalRepository {

    private final List<Animal> animals = new ArrayList<>();

    public FakeAnimalRepository() {
        animals.add(new Animal(1L, "Bella", "Dog", 3, "Healthy", false));
        animals.add(new Animal(2L, "Milo", "Cat", 2, "Needs Vaccination", false));
        animals.add(new Animal(3L, "Lucy", "Dog", 1, "Healthy", true));
    }

    public List<Animal> findAll() {
        return new ArrayList<>(animals);
    }

    public Optional<Animal> findById(Long id) {
        return animals.stream()
                .filter(a -> a.id.equals(id))
                .findFirst();
    }

    public Animal save(Animal animal) {
        long nextId = animals.stream()
                .mapToLong(a -> a.id)
                .max()
                .orElse(0L) + 1;
        animal.id = nextId;
        animals.add(animal);
        return animal;
    }

    public Animal update(Animal animal) {
        Optional<Animal> existingOpt = findById(animal.id);
        if (existingOpt.isPresent()) {
            Animal existing = existingOpt.get();
            existing.name = animal.name;
            existing.species = animal.species;
            existing.age = animal.age;
            existing.healthStatus = animal.healthStatus;
            existing.adopted = animal.adopted;
            return existing;
        } else {
            throw new RuntimeException("Animal not found");
        }
    }

    public boolean delete(Long id) {
        return animals.removeIf(a -> a.id.equals(id));
    }

    public boolean adoptAnimal(Long id) {
        Optional<Animal> existingOpt = findById(id);
        if (existingOpt.isPresent()) {
            Animal existing = existingOpt.get();
            if (existing.adopted) {
                return false;
            }
            existing.adopted = true;
            return true;
        }
        return false;
    }

    public Animal updateHealthStatus(Long animalId, String status) {
        Optional<Animal> existing = findById(animalId);
        if (existing.isPresent()) {
            Animal a = existing.get();
            a.healthStatus = status;
            return a;
        }
        throw new RuntimeException("Animal not found");
    }
}