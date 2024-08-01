package utils;

import com.example.HRMSAvisoft.attribute.Attribute;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class AttributesSerializer extends JsonSerializer<Map<? extends Attribute, String>> {
    private static final Logger logger = Logger.getLogger(AttributesSerializer.class.getName());

    @Override
    public void serialize(Map<? extends Attribute, String> attributes, JsonGenerator gen, SerializerProvider serializers) throws IOException, IOException {
        gen.writeStartObject();
        for (Map.Entry<? extends Attribute, String> entry : attributes.entrySet()) {
            Attribute attribute = entry.getKey();
            String attributeKey = (attribute != null && attribute.getAttributeKey() != null) ? attribute.getAttributeKey() : "attribute_" + attribute.getAttributeId();
            String attributeValue = entry.getValue() != null ? entry.getValue() : "null";

            if (attributeKey.startsWith("attribute_")) {
                logger.warning("Attribute key is missing. Using fallback: " + attributeKey);
            } else {
                logger.info("Serializing attribute with key: " + attributeKey);
            }

            gen.writeStringField(attributeKey, attributeValue);
        }
        gen.writeEndObject();
    }
}