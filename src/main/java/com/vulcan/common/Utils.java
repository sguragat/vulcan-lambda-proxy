package com.vulcan.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by sg on 29/07/2018.
 */
public class Utils {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String asJson(Object pojo, ObjectMapper objectMapper) {
        return asJson(pojo, objectMapper, false);
    }

    public static String asJson(Object pojo, ObjectMapper objectMapper, boolean pretty) {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
            }
            return objectMapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Failed to convert POJO (%s >> %s) into Json-String", pojo.getClass().getName(), pojo), e);
        }
    }
}
