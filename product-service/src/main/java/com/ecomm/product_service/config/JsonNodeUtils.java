package com.ecomm.product_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class JsonNodeUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static <T> T getObjectFromFile(Resource resource,Class<T> classType) throws IOException {

        return objectMapper.readValue(resource.getInputStream(),classType);
    }
    public static <T> T readJsonFile(final String fileName, Class<T> classType) throws IOException {
        return getObjectFromFile(new ClassPathResource(fileName),classType);
    }
    public static <T> List<T> getListFromFile(Resource resource, TypeReference<List<T>> typeReference) throws IOException {
        InputStream inputStream = resource.getInputStream();
        return objectMapper.readValue(inputStream, typeReference);

    }
    public static <T> List<T> readJsonFileAsList(final String fileName, TypeReference<List<T>> typeReference) throws IOException {
        return getListFromFile(new ClassPathResource(fileName), typeReference);
    }
}
