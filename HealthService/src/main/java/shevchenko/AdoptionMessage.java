package shevchenko;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDateTime;

/**
 * ВАЖЛИВО: Цей клас дубльований в adoption-service та health-service
 * При зміні треба оновити ОБА проєкти!
 *
 * Повідомлення про усиновлення тварини для RabbitMQ
 */
@RegisterForReflection
public class AdoptionMessage {

    @JsonProperty("animal_id")
    private Long animalId;

    @JsonProperty("adopter_name")
    private String adopterName;

    @JsonProperty("adoption_id")
    private Long adoptionId;

    @JsonProperty("adoption_date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime adoptionDate;

    @JsonProperty("event_type")
    private String eventType; // ADOPTION_CREATED, ADOPTION_CANCELLED, ADOPTION_STARTED, ADOPTION_COMPLETED

    @JsonProperty("timestamp")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    // Конструктор за замовчуванням (потрібен для Jackson десеріалізації)
    public AdoptionMessage() {
        this.timestamp = LocalDateTime.now();
    }

    // Простий конструктор (для зворотної сумісності з існуючим кодом)
    public AdoptionMessage(Long animalId, String adopterName) {
        this.animalId = animalId;
        this.adopterName = adopterName;
        this.eventType = "ADOPTION_CREATED";
        this.adoptionDate = LocalDateTime.now();
        this.timestamp = LocalDateTime.now();
    }

    // Повний конструктор
    public AdoptionMessage(Long animalId, String adopterName, Long adoptionId,
                           LocalDateTime adoptionDate, String eventType) {
        this.animalId = animalId;
        this.adopterName = adopterName;
        this.adoptionId = adoptionId;
        this.adoptionDate = adoptionDate;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    // Статичні методи-фабрики
    public static AdoptionMessage adoptionCreated(Long animalId, Long adoptionId,
                                                  String adopterName, LocalDateTime adoptionDate) {
        return new AdoptionMessage(animalId, adopterName, adoptionId, adoptionDate, "ADOPTION_CREATED");
    }

    public static AdoptionMessage adoptionCancelled(Long animalId, Long adoptionId) {
        return new AdoptionMessage(animalId, null, adoptionId, null, "ADOPTION_CANCELLED");
    }

    public static AdoptionMessage adoptionStarted(Long animalId, Long adoptionId,
                                                  String adopterName, LocalDateTime adoptionDate) {
        return new AdoptionMessage(animalId, adopterName, adoptionId, adoptionDate, "ADOPTION_STARTED");
    }

    public static AdoptionMessage adoptionCompleted(Long animalId, Long adoptionId) {
        return new AdoptionMessage(animalId, null, adoptionId, null, "ADOPTION_COMPLETED");
    }

    // Getters and Setters
    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }

    public Long getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Long adoptionId) {
        this.adoptionId = adoptionId;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AdoptionMessage{" +
                "animalId=" + animalId +
                ", adopterName='" + adopterName + '\'' +
                ", adoptionId=" + adoptionId +
                ", adoptionDate=" + adoptionDate +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}