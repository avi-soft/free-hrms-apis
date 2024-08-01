package utils;

import com.example.HRMSAvisoft.entity.EmployeeAttribute;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class EmployeeAttributesSerializer extends JsonSerializer<Map<EmployeeAttribute, String>> {
    @Override
    public void serialize(Map<EmployeeAttribute, String> attributes, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<EmployeeAttribute, String> entry : attributes.entrySet()) {
            gen.writeStringField(entry.getKey().getAttributeKey(), entry.getValue());
        }
        gen.writeEndObject();
    }
}

