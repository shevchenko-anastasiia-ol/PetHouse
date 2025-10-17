package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FakeAdoptionRepository {

    private final List<Adoption> records = new ArrayList<>();

    public FakeAdoptionRepository() {
        records.add(new Adoption(1L, 3L, "Olena Ivanova", "+380671234567", LocalDate.of(2024,1,10), "Family with children"));
    }

    public List<Adoption> findAll() {
        return new ArrayList<>(records);
    }

    public Optional<Adoption> findById(Long id) {
        return records.stream().filter(r -> r.id.equals(id)).findFirst();
    }

    public Optional<Adoption> findByAnimalId(Long animalId) {
        return records.stream().filter(r -> r.animalId.equals(animalId)).findFirst();
    }

    public Adoption save(Adoption rec) {
        long nextId = records.stream().map(r -> r.id).max(Comparator.naturalOrder()).orElse(0L) + 1;
        rec.id = nextId;
        if (rec.adoptionDate == null) rec.adoptionDate = LocalDate.now();
        records.add(rec);
        return rec;
    }

    public Adoption update(Adoption rec) {
        Optional<Adoption> existing = findById(rec.id);
        if (existing.isPresent()) {
            Adoption e = existing.get();
            e.animalId = rec.animalId;
            e.adopterName = rec.adopterName;
            e.adopterContact = rec.adopterContact;
            e.adoptionDate = rec.adoptionDate;
            e.notes = rec.notes;
            return e;
        }
        throw new RuntimeException("Adoption record not found");
    }

    public boolean delete(Long id) {
        return records.removeIf(r -> r.id.equals(id));
    }
}
