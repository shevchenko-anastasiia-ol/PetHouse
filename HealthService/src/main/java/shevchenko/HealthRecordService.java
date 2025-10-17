package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HealthRecordService {
    @Inject
    FakeHealthRepository repository;

    @Inject
    @RestClient
    AnimalRestClient animalClient;

    public HealthRecord create(HealthRecord record) {
        Animal animal = animalClient.getById(record.animalId);
        if (animal == null) throw new RuntimeException("Animal not found");

        if (record.healthStatus == null || record.healthStatus.isBlank()) {
            record.healthStatus = "Healthy";
        }

        // Зберігаємо медичний запис
        HealthRecord saved = repository.save(record);

        // Оновлюємо статус тварини у AnimalService
        animalClient.updateHealthStatus(record.animalId, record.healthStatus);

        return saved;
    }

    private String determineHealthStatus(HealthRecord record) {
        if (record.diagnosis != null && record.diagnosis.toLowerCase().contains("chronic")) {
            return "Має хронічні хвороби";
        } else if (record.treatment != null && record.treatment.toLowerCase().contains("vaccin")) {
            return "Потребує вакцинації";
        } else if (record.diagnosis != null && record.diagnosis.toLowerCase().contains("treatment")) {
            return "Проходить лікування";
        } else {
            return "Здорова";
        }
    }

    public List<HealthRecord> getAll() {
        return repository.findAll();
    }

    public Optional<HealthRecord> getById(Long id) {
        return repository.findById(id);
    }

    public List<HealthRecord> getByAnimalId(Long animalId) {
        return repository.findByAnimalId(animalId);
    }

    public Optional<HealthRecord> getLatestByAnimalId(Long animalId) {
        return repository.findLatestByAnimalId(animalId);
    }

    public HealthRecord update(HealthRecord record) {
        return repository.update(record);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}
