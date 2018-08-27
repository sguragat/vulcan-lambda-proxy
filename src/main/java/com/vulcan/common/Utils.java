package com.vulcan.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by sg on 29/07/2018.
 */
public class Utils {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String asJson(Object pojo) {
        return asJson(pojo, OBJECT_MAPPER, false);
    }

    public static String asJson(Object pojo, ObjectMapper objectMapper) {
        return asJson(pojo, objectMapper, false);
    }

    public static String asJson(Object pojo, boolean pretty) {
        return asJson(pojo, OBJECT_MAPPER, pretty);
    }

    public static String asJson(Object pojo, ObjectMapper objectMapper, boolean pretty) {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
            }
            return objectMapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Failed to convert POJO (%s, %s) into JSON-String", pojo.getClass().getName(), pojo), e);
        }
    }

    public static <E> E asPojo(Class<E> pojoType, String json) {
        return asPojo(pojoType, json, OBJECT_MAPPER);
    }

    public static <E> E asPojo(Class<E> pojoType, String json, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, pojoType);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to convert JSON-String (%s) into POJO (%s)", json, pojoType), e);
        }
    }
}
