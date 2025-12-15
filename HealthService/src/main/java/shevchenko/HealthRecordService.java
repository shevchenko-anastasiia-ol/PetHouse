package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class HealthRecordService {
    private static final Logger LOG = Logger.getLogger(HealthRecordService.class);

    @Inject
    @RestClient
    AnimalRestClient animalClient;

    @Incoming("adoption-events")
    @Blocking
    @Transactional
    public void onAdoptionEvent(AdoptionMessage message) {
        LOG.infof("📩 Received adoption event: %s", message);
        LOG.infof("Animal ID: %d, Event Type: %s, Adopter: %s",
                message.getAnimalId(), message.getEventType(), message.getAdopterName());


        try {
            String eventType = message.getEventType();

            if ("ADOPTION_CREATED".equals(eventType) || "ADOPTION_STARTED".equals(eventType)) {
                handleAdoptionCreated(message);
            } else if ("ADOPTION_CANCELLED".equals(eventType)) {
                handleAdoptionCancelled(message);
            } else if ("ADOPTION_COMPLETED".equals(eventType)) {
                handleAdoptionCompleted(message);
            } else {
                LOG.warnf("⚠️ Unknown event type: %s", eventType);
            }
        } catch (Exception e) {
            LOG.errorf(e, "❌ Error processing adoption message: %s", message);
            throw e; // RabbitMQ retry mechanism
        }
    }

    /**
     * Обробка створення усиновлення - встановлюємо isAdopted = true
     */
    private void handleAdoptionCreated(AdoptionMessage message) {
        Long animalId = message.getAnimalId();
        LOG.infof("🔄 Handling ADOPTION_CREATED for animal ID=%d", animalId);

        List<HealthRecord> records = HealthRecord.list("animalId", animalId);
        LOG.infof("Found %d health record(s) for animal ID=%d", records.size(), animalId);

        if (records.isEmpty()) {
            LOG.warnf("⚠️ No health records found for animal ID=%d, creating new one", animalId);
            createHealthRecordForAdoption(message);
        } else {
            int updatedCount = 0;
            for (HealthRecord record : records) {
                if (!record.isAdopted) {
                    record.isAdopted = true;
                    record.persist();
                    updatedCount++;
                }
            }
            // Додатковий flush на всякий випадок
            HealthRecord.getEntityManager().flush();

            LOG.infof("✅ Updated %d health record(s) for animal ID=%d (isAdopted=true)",
                    updatedCount, animalId);
        }
    }

    /**
     * Обробка скасування усиновлення - встановлюємо isAdopted = false
     */
    private void handleAdoptionCancelled(AdoptionMessage message) {
        Long animalId = message.getAnimalId();

        List<HealthRecord> records = HealthRecord.list("animalId", animalId);

        int updatedCount = 0;
        for (HealthRecord record : records) {
            if (record.isAdopted) {
                record.isAdopted = false;
                record.persist();
                updatedCount++;
            }
        }

        LOG.infof("✅ Updated %d health record(s) for animal ID=%d (isAdopted=false)",
                updatedCount, animalId);
    }

    /**
     * Обробка завершення усиновлення
     */
    private void handleAdoptionCompleted(AdoptionMessage message) {
        LOG.infof("ℹ️ Adoption completed for animal ID=%d", message.getAnimalId());
        // Можна додати додаткову логіку
    }

    /**
     * Створення health record якщо не існує (при усиновленні)
     */
    private void createHealthRecordForAdoption(AdoptionMessage message) {
        HealthRecord record = new HealthRecord();
        record.animalId = message.getAnimalId();
        record.isAdopted = true;
        record.visitDate = LocalDate.now();
        record.healthStatus = "Healthy";
        record.notes = "Auto-created on adoption. Adopter: " + message.getAdopterName();

        record.persist();

        LOG.infof("✅ Created new health record for adopted animal ID=%d", message.getAnimalId());
    }

    @Transactional
    public HealthRecord create(HealthRecord record) {
        LOG.infof("HealthRecordService.create() called with animalId: %d", record.animalId);
        try {
            Animal animal = animalClient.getById(record.animalId);
            LOG.infof("Animal retrieved: %s", (animal != null ? animal.name : "null"));
            System.out.println("Animal retrieved: " + (animal != null ? animal.name : "null"));
            if (animal == null) throw new RuntimeException("Animal not found");

            if (record.id != null) {
                HealthRecord existing = HealthRecord.findById(record.id);
                if (existing != null) {
                    LOG.info("Health record exists, updating instead");
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

            LOG.info("Persisting health record...");
            record.persist();
            LOG.infof("Health record persisted with ID: %d", record.id);

            LOG.infof("Updating animal health status to: %s", record.healthStatus);
            animalClient.updateHealthStatus(record.animalId, record.healthStatus);
            LOG.info("Animal health status updated successfully");

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
