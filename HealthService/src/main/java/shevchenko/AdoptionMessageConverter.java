package shevchenko;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import java.lang.reflect.Type;

/**
 * Конвертер для перетворення JsonObject у AdoptionMessage
 */
@ApplicationScoped
public class AdoptionMessageConverter implements MessageConverter {

    private static final Logger LOG = Logger.getLogger(AdoptionMessageConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        boolean canConvert = in.getPayload() instanceof JsonObject
                && target.equals(AdoptionMessage.class);

        if (canConvert) {
            LOG.debugf("✅ Can convert JsonObject to AdoptionMessage");
        }

        return canConvert;
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        try {
            JsonObject json = (JsonObject) in.getPayload();

            LOG.infof("🔄 Converting JsonObject to AdoptionMessage: %s", json.encode());

            AdoptionMessage message = objectMapper.readValue(
                    json.encode(),
                    AdoptionMessage.class
            );

            LOG.infof("✅ Successfully converted to: %s", message);

            return in.withPayload(message);

        } catch (Exception e) {
            LOG.errorf(e, "❌ Failed to convert message");
            throw new RuntimeException("Failed to convert message", e);
        }
    }
}