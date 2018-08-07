package com.vulcan.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 02/08/2018.
 */
class RequestBody {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private RequestBody() {

    }

    public static <E> E getEntity(Request request, Class<E> entityType, ObjectMapper objectMapper) {
        String contentType = request.getHeaders().contentType();
        if (contentType == null) {
            contentType = CONTENT_TYPE_APPLICATION_JSON;
        }
        switch (contentType) {
            case CONTENT_TYPE_APPLICATION_JSON:
                return getJsonEntity(request, entityType, objectMapper);
            case CONTENT_TYPE_X_WWW_FORM_URLENCODED:
                return getXFormEntity(request, entityType, objectMapper);
            default:
                throw new IllegalStateException("Content-Type: " + contentType + " is not supported");
        }
    }

    public static <E> E getJsonEntity(Request request, Class<E> entityType, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(request.getBody(), entityType);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert Json-String into Map", e);
        }
    }

    public static <E> List<E> getJsonEntityList(Request request, Class<E> entityType, ObjectMapper objectMapper) {
        throw new IllegalStateException("getJsonEntityList not implemented yet");
    }

    public static <E> E getXFormEntity(Request request, Class<E> entityType, ObjectMapper objectMapper) {
        Map<String, Object> entity = new HashMap<>();
        String body = request.getBody();
        int startIndex = 0;
        int equalSignIndex;
        int andSignIndex;

        while ((equalSignIndex = body.indexOf('=', startIndex)) > -1) {
            String key = body.substring(startIndex, equalSignIndex);
            startIndex = equalSignIndex + 1;
            andSignIndex = body.indexOf('&', startIndex);
            String value;
            if (andSignIndex > -1) {
                value = body.substring(startIndex, andSignIndex);
                startIndex = (andSignIndex + 1);
            } else {
                value = body.substring(startIndex); // till end
                startIndex += value.length();
            }
            entity.put(key, value);
        }

        if (entityType.equals(Map.class)) { // TODO : revisit
           return (E) entity;
        }
        return objectMapper.convertValue(entity, entityType);
    }
}
