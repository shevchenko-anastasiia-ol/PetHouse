package shevchenko;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "adoptions")
public class Adoption extends PanacheEntity {
    @Column(name = "animalid")
    public Long animalId;
    
    @Column(name = "adoptername")
    public String adopterName;
    
    @Column(name = "adoptercontact")
    public String adopterContact;
    
    @Column(name = "adoptiondate")
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
