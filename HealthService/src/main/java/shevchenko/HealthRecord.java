package shevchenko;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "health_records")
public class HealthRecord extends PanacheEntity {
    @Column(name = "animalid")
    public Long animalId;
    
    @Column(name = "visitdate")
    public LocalDate visitDate;
    
    @Column(name = "vetname")
    public String vetName;
    
    public String diagnosis;
    public String treatment;
    public String notes;
    
    @Column(name = "nextappointment")
    public LocalDate nextAppointment;
    
    @Column(name = "healthstatus")
    public String healthStatus;

    public HealthRecord() {}

    public HealthRecord(Long id, Long animalId, LocalDate visitDate, String vetName, String diagnosis, String treatment, String notes, LocalDate nextAppointment, String healthStatus) {
        this.id = id;
        this.animalId = animalId;
        this.visitDate = visitDate;
        this.vetName = vetName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.nextAppointment = nextAppointment;
        this.healthStatus = healthStatus;
    }
}
