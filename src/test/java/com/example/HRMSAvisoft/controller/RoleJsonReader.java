package com.example.HRMSAvisoft.controller;
import com.example.HRMSAvisoft.entity.Privilege;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoleJsonReader {

    public Map<String, Object> readFile(String path) throws IOException {
        // Load the JSON file from the resources folder
        InputStream inputStream = RoleJsonReader.class.getClassLoader().getResourceAsStream("payloads/" + path + ".json");

        if (inputStream == null) {
            throw new IOException("File not found: " + path + ".json");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);

        // Convert JSON data to a Map
        Map<String, Object> dataMap = objectMapper.convertValue(rootNode, Map.class);

        // Parse privilege into a Set of Privilege enum values
        List<String> privilegeStrings = (List<String>) dataMap.get("privilege");
        Set<Privilege> privileges = new HashSet<>();
        for (String privilegeString : privilegeStrings) {
            Privilege privilege = Privilege.valueOf(privilegeString);
            privileges.add(privilege);
        }
        dataMap.put("privilege", privileges);
        // Close the input stream
        inputStream.close();

        return dataMap;
    }
}

