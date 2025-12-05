package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.messaging.Message;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AdoptionEventConsumer {

    private static final Logger Log = Logger.getLogger(AdoptionEventConsumer.class);

    @Incoming("adoption-in")
    @Outgoing("adoption-processed")
    public Message<AdoptionMessage> processAdoption(Message<JsonObject> message) {
        AdoptionMessage adoptionMessage = message.getPayload().mapTo(AdoptionMessage.class);
        
        AdoptionMessage adoption = new AdoptionMessage(
            adoptionMessage.animalId,
            adoptionMessage.adopterName
        );
        
        Log.info("Processing received adoption: " + adoption);
        
        return message.withPayload(adoption);
    }
}
