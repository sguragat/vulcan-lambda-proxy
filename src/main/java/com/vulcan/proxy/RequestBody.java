package com.vulcan.proxy;

import com.vulcan.converter.BodyConverter;

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

    public static Map<String, Object> getMap(Request request, BodyConverter bodyConverter) {
        String contentType = request.getHeaders().contentType();
        if (contentType == null) {
            contentType = CONTENT_TYPE_APPLICATION_JSON;
        }
        switch (contentType) {
            case CONTENT_TYPE_APPLICATION_JSON:
                return readMapJsonBody(request, bodyConverter);
            case CONTENT_TYPE_X_WWW_FORM_URLENCODED:
                return readMapFormBody(request, bodyConverter);
            default:
                throw new IllegalStateException("Content-Type: " + contentType + " is not supported");
        }
    }

    public static <E> E getEntity(Request request, BodyConverter bodyConverter) {
        String contentType = request.getHeaders().contentType();
        if (contentType == null) {
            contentType = CONTENT_TYPE_APPLICATION_JSON;
        }
        switch (contentType) {
            case CONTENT_TYPE_APPLICATION_JSON:
                return readEntityJsonBody(request, bodyConverter);
            case CONTENT_TYPE_X_WWW_FORM_URLENCODED:
                return readEntityFormBody(request, bodyConverter);
            default:
                throw new IllegalStateException("Content-Type: " + contentType + " is not supported");
        }
    }

    private static <E> E readEntityJsonBody(Request request, BodyConverter bodyConverter) {
        try {
            return bodyConverter.jsonToObject(request.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Entity from request body (application/json)", e);
        }
    }

    private static <E> E readEntityFormBody(Request request, BodyConverter bodyConverter) {
        try {
            return bodyConverter.formToObject(request.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Entity from request body (application/x-www-form-urlencoded)", e);
        }
    }

    private static Map<String, Object> readMapJsonBody(Request request, BodyConverter bodyConverter) {
        try {
            return bodyConverter.jsonToMap(request.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Map from request body (application/json)", e);
        }
    }

    private static Map<String, Object> readMapFormBody(Request request, BodyConverter bodyConverter) {
        try {
            return bodyConverter.formToMap(request.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Map from request body (application/x-www-form-urlencoded)", e);
        }
    }

    public static <E> List<E> getList(Request request, BodyConverter bodyConverter) {
        throw new IllegalStateException("getJsonEntityList not implemented yet");
    }
}
