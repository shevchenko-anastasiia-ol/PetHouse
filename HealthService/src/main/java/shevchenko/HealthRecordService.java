package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HealthRecordService {
    @Inject
    @RestClient
    AnimalRestClient animalClient;

    @Transactional
    public HealthRecord create(HealthRecord record) {
        System.out.println("HealthRecordService.create() called with animalId: " + record.animalId);
        try {
            Animal animal = animalClient.getById(record.animalId);
            System.out.println("Animal retrieved: " + (animal != null ? animal.name : "null"));
            if (animal == null) throw new RuntimeException("Animal not found");

            // For new entities, ensure ID is null so Panache generates it
            if (record.id != null) {
                HealthRecord existing = HealthRecord.findById(record.id);
                if (existing != null) {
                    System.out.println("Health record exists, updating instead");
                    return update(record);
                }
            }
            record.id = null;

            if (record.healthStatus == null || record.healthStatus.isBlank()) {
                record.healthStatus = "Healthy";
            }

            if (record.visitDate == null) {
                record.visitDate = LocalDate.now();
            }

            // Зберігаємо медичний запис
            System.out.println("Persisting health record...");
            record.persist();
            System.out.println("Health record persisted with ID: " + record.id);

            // Оновлюємо статус тварини у AnimalService
            System.out.println("Updating animal health status to: " + record.healthStatus);
            animalClient.updateHealthStatus(record.animalId, record.healthStatus);
            System.out.println("Animal health status updated successfully");

            return record;
        } catch (Exception e) {
            System.err.println("Error creating health record: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create health record: " + e.getMessage(), e);
        }
    }


    public List<HealthRecord> getAll() {
        return HealthRecord.listAll();
    }

    public Optional<HealthRecord> getById(Long id) {
        return HealthRecord.findByIdOptional(id);
    }

    public List<HealthRecord> getByAnimalId(Long animalId) {
        return HealthRecord.find("animalId", animalId).list();
    }

    public Optional<HealthRecord> getLatestByAnimalId(Long animalId) {
        return HealthRecord.find("animalId = ?1 ORDER BY visitDate DESC", animalId).firstResultOptional();
    }

    @Transactional
    public HealthRecord update(HealthRecord record) {
        HealthRecord existing = HealthRecord.findById(record.id);
        if (existing == null) {
            throw new RuntimeException("Health record not found");
        }
        existing.visitDate = record.visitDate;
        existing.vetName = record.vetName;
        existing.diagnosis = record.diagnosis;
        existing.treatment = record.treatment;
        existing.notes = record.notes;
        existing.nextAppointment = record.nextAppointment;
        existing.healthStatus = record.healthStatus;
        existing.persist();
        return existing;
    }

    @Transactional
    public boolean delete(Long id) {
        return HealthRecord.deleteById(id);
    }
}
