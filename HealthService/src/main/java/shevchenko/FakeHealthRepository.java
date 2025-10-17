package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FakeHealthRepository {

    private final List<HealthRecord> records = new ArrayList<>();

    public FakeHealthRepository() {
        records.add(new HealthRecord(1L, 1L, LocalDate.of(2024,6,1), "Dr. Petrenko", "Checkup", "Vaccination", "All good", LocalDate.of(2025,6,1), "Taking treatment"));
        records.add(new HealthRecord(2L, 2L, LocalDate.of(2024,7,10), "Dr. Bondarenko", "Ear infection", "Antibiotics", "Control in 10 days", null, "Need vaccination"));
    }

    public List<HealthRecord> findAll() {
        return new ArrayList<>(records);
    }

    public Optional<HealthRecord> findById(Long id) {
        return records.stream().filter(r -> r.id.equals(id)).findFirst();
    }

    public List<HealthRecord> findByAnimalId(Long animalId) {
        var list = new ArrayList<HealthRecord>();
        for (HealthRecord r : records) if (r.animalId.equals(animalId)) list.add(r);
        return list;
    }

    public Optional<HealthRecord> findLatestByAnimalId(Long animalId) {
        return records.stream()
                .filter(r -> r.animalId.equals(animalId))
                .max(Comparator.comparing(r -> r.visitDate));
    }

    public HealthRecord save(HealthRecord rec) {
        long nextId = records.stream().map(r -> r.id).max(Comparator.naturalOrder()).orElse(0L) + 1;
        rec.id = nextId;
        if (rec.visitDate == null) rec.visitDate = LocalDate.now();
        records.add(rec);
        return rec;
    }

    public HealthRecord update(HealthRecord rec) {
        Optional<HealthRecord> existing = findById(rec.id);
        if (existing.isPresent()) {
            HealthRecord e = existing.get();
            e.visitDate = rec.visitDate;
            e.vetName = rec.vetName;
            e.diagnosis = rec.diagnosis;
            e.treatment = rec.treatment;
            e.notes = rec.notes;
            e.nextAppointment = rec.nextAppointment;
            return e;
        }
        throw new RuntimeException("Health record not found");
    }

    public boolean delete(Long id) {
        return records.removeIf(r -> r.id.equals(id));
    }
}
