package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class JsonUtils {
    public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {

        // Get the JSON String
        String jsonContent = FileUtils.readFileToString(
            new File(jsonFilePath)
        );

        // Map the Json string to HashMap
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(
                jsonContent, new TypeReference<>() {
                }
        );

        return data;

    }
}
