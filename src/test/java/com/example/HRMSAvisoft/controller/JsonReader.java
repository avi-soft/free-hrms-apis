package com.example.HRMSAvisoft.controller;


import com.example.HRMSAvisoft.entity.AddressType;
import com.example.HRMSAvisoft.entity.Privilege;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonReader {

    public Map<String, Object> readFile(String path) throws IOException {
        InputStream inputStream = RoleJsonReader.class.getClassLoader().getResourceAsStream("payloads/" + path + ".json");

        if (inputStream == null) {
            throw new IOException("File not found: " + path + ".json");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);

        // Convert JSON data to a Map
        Map<String, Object> dataMap = objectMapper.convertValue(rootNode, Map.class);

        // Close the input stream
        inputStream.close();

        return dataMap;

//        // Parse addressType into enum value
//        String addressTypeString = (String) dataMap.get("addressType");
//        AddressType addressType = AddressType.valueOf(addressTypeString);
//
//        // Replace the string value with the enum value
//        dataMap.put("addressType", addressType);

    }

}