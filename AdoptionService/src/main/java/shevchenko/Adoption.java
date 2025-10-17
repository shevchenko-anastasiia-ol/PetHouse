package shevchenko;

import java.time.LocalDate;

public class Adoption {
    public Long id;
    public Long animalId;
    public String adopterName;
    public String adopterContact;
    public LocalDate adoptionDate;
    public String notes;

    public Adoption() {}

    public Adoption(Long id, Long animalId, String adopterName, String adopterContact, LocalDate adoptionDate, String notes) {
        this.id = id;
        this.animalId = animalId;
        this.adopterName = adopterName;
        this.adopterContact = adopterContact;
        this.adoptionDate = adoptionDate;
        this.notes = notes;
    }
}
