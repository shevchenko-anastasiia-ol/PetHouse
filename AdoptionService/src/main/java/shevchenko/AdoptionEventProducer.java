package shevchenko;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.inject.Inject;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class AdoptionEventProducer {

    @Inject
    @Channel("adoption-out")
    MutinyEmitter<AdoptionMessage> adoptionEmitter;

    public Uni<Void> send(AdoptionMessage message) {
        return adoptionEmitter.send(message);
    }
}
