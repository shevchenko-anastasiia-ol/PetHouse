package shevchenko;

import java.time.LocalDate;

public class HealthRecord {
    public Long id;
    public Long animalId;
    public LocalDate visitDate;
    public String vetName;
    public String diagnosis;
    public String treatment;
    public String notes;
    public LocalDate nextAppointment;
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
