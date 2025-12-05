package shevchenko;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class AdoptionMessage {

    @JsonProperty("animalId")
    public Long animalId;
    
    @JsonProperty("adopterName")
    public String adopterName;
    
    @JsonProperty("adoptionDate")
    public LocalDateTime adoptionDate;

    public AdoptionMessage() {}

    public AdoptionMessage(Long animalId, String adopterName) {
        this.animalId = animalId;
        this.adopterName = adopterName;
        this.adoptionDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "AdoptionMessage{" +
                "animalId=" + animalId +
                ", adopterName='" + adopterName + '\'' +
                ", adoptionDate=" + adoptionDate +
                '}';
    }
}