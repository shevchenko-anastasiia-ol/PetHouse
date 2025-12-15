package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.inject.Inject;
import java.time.LocalDateTime;



@ApplicationScoped
public class AdoptionService {
    private static final Logger Log = Logger.getLogger(AdoptionService.class);
    
    @RestClient
    AnimalRestClient animalClient;

    @Inject
    AdoptionEventProducer adoptionEventProducer;


    public List<Adoption> getAll() {
        return Adoption.listAll();
    }

    public Optional<Adoption> getById(Long id) {
        return Adoption.findByIdOptional(id);
    }

    public Optional<Adoption> getByAnimalId(Long animalId) {
        return Adoption.find("animalId", animalId).firstResultOptional();
    }

    public Optional<Adoption> findByAdopterAndAnimalIdsOptional(String adopterName, Long animalId) {
        return Adoption.find("adopterName = ?1 and animalId = ?2", adopterName, animalId).firstResultOptional();
    }

    @Transactional
    public Adoption createAdoption(Adoption adoption) {
        adoption.id = null;
        if (adoption.adoptionDate == null) {
            adoption.adoptionDate = LocalDate.now();
        }

        adoption.persistAndFlush();

        sendAdoptionMessage(adoption, "ADOPTION_CREATED");


        return adoption;
    }

    @Transactional
    public Adoption updateAdoption(Adoption adoption) {
        Adoption existing = Adoption.findById(adoption.id);
        if (existing == null) {
            throw new RuntimeException("Adoption record not found");
        }
        existing.animalId = adoption.animalId;
        existing.adopterName = adoption.adopterName;
        existing.adopterContact = adoption.adopterContact;
        existing.adoptionDate = adoption.adoptionDate;
        existing.notes = adoption.notes;
        existing.persist();
        return existing;
    }

    @Transactional
    public boolean deleteAdoption(Long id) {
        Adoption adoption = Adoption.findById(id);
        if (adoption != null) {
            // Відправка повідомлення про скасування
            sendAdoptionMessage(adoption, "ADOPTION_CANCELLED");
            return Adoption.deleteById(id);
        }
        return false;
    }

    @Transactional
    public Adoption adoptAnimal(Adoption adoption) {
        adoption.id = null;
        if (adoption.adoptionDate == null) {
            adoption.adoptionDate = LocalDate.now();
        }
        adoption.persistAndFlush();

        Response r = animalClient.adoptAnimal(adoption.animalId);
        if (r.getStatus() == 200) {
            System.out.println("Adoption confirmed: " + r.readEntity(String.class));

            // Відправка повідомлення про успішне усиновлення
            sendAdoptionMessage(adoption, "ADOPTION_CREATED");
            

        } else if (r.getStatus() == 404) {
            throw new RuntimeException("Animal not found");
        } else if (r.getStatus() == 409) {
            throw new RuntimeException("Already adopted");
        } else {
            throw new RuntimeException("Adoption failed: " + r.getStatus());
        }

        return adoption;
    }

    @Transactional
    public Adoption start(String adopterName, Long animalId) {
        Log.infof("Starting adoption for %s with animal %s", adopterName, animalId);

        Optional<Adoption> adoptionOptional = findByAdopterAndAnimalIdsOptional(adopterName, animalId);
        Adoption adoption;

        if (adoptionOptional.isPresent()) {
            adoption = adoptionOptional.get();
            if (adoption.adoptionDate == null) {
                adoption.adoptionDate = LocalDate.now();
            }
            adoption.persist();
        } else {
            adoption = new Adoption();
            adoption.adopterName = adopterName;
            adoption.animalId = animalId;
            adoption.adoptionDate = LocalDate.now();
            adoption.persist();
        }

        // Відправка повідомлення про початок процесу усиновлення
        sendAdoptionMessage(adoption, "ADOPTION_STARTED");

        return adoption;
    }

    @Transactional
    public Adoption end(String adopterName, Long animalId) {
        Log.infof("Ending adoption for %s with animal %s", adopterName, animalId);

        Adoption adoption = findByAdopterAndAnimalIdsOptional(adopterName, animalId)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("Adoption not found"));

        if (adoption.adoptionDate == null) {
            Log.warn("Adoption is not confirmed: " + adoption);
        }

        Response animalResponse = animalClient.getAnimalById(adoption.animalId);
        if (animalResponse.getStatus() == 200) {
            Log.infof("Animal information retrieved for adoption: %s", adoption);
        }

        // Відправка повідомлення про завершення
        sendAdoptionMessage(adoption, "ADOPTION_COMPLETED");

        return adoption;
    }

    /**
     * Універсальний метод для відправки повідомлень про усиновлення
     */
    private void sendAdoptionMessage(Adoption adoption, String eventType) {
        LocalDateTime adoptionDateTime = adoption.adoptionDate != null
                ? adoption.adoptionDate.atStartOfDay()
                : LocalDateTime.now();

        AdoptionMessage message = new AdoptionMessage(
                adoption.animalId,
                adoption.adopterName,
                adoption.id,
                adoptionDateTime,
                eventType
        );

        adoptionEventProducer.send(message)
                .subscribe().with(
                        result -> Log.infof("✅ [ADOPTION SERVICE] Message sent: %s for Animal ID=%d",
                                eventType, adoption.animalId),
                        failure -> Log.errorf("❌ [ADOPTION SERVICE] Failed to send message: %s",
                                failure.getMessage())
                );
    }


}
