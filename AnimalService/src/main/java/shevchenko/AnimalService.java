package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AnimalService {

    @Inject
    FakeAnimalRepository repository;

    public List<Animal> getAll() {
        return repository.findAll();
    }

    public Optional<Animal> getById(Long id) {
        return repository.findById(id);
    }

    public Animal createAnimal(Animal animal) {
        return repository.save(animal);
    }

    public Animal updateAnimal(Animal animal) {
        return repository.update(animal);
    }

    public boolean deleteAnimal(Long id) {
        return repository.delete(id);
    }

    // Метод для зміни статусу adopted (викликається через gRPC/REST з Adoption Service)
    public Animal markAsAdopted(Long id) {
        Optional<Animal> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Animal not found");
        }

        Animal animal = opt.get();
        if (animal.adopted) {
            throw new RuntimeException("Already adopted");
        }

        animal.adopted = true;
        return repository.update(animal);
    }

    public Animal updateHealthStatus(Long id, String status) {
        return repository.updateHealthStatus(id, status);
    }
}