package com.vulcan.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sg on 15/07/2018.
 */
public class Response extends HashMap<String, Object> {

    private Response() {}

    public Integer getStatusCode() {
        return (Integer) get("statusCode");
    }

    public void setStatusCode(int statusCode) {
        put("statusCode", statusCode);
    }

    public Map<String, Object> getHeaders() {
        return (Map<String, Object>) get("headers");
    }

    public void setHeaders(Map<String, Object> headers) {
        put("headers", headers);
    }

    public String getBody() {
        return (String) get("body");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public Boolean getBase64Encoded() {
        return (Boolean) get("base64Encoded");
    }

    public void setBase64Encoded(boolean base64Encoded) {
        put("base64Encoded", base64Encoded);
    }

    private static Builder def() {
        return new Builder().base64Encoded(false);
    }

    public static Builder ok() {
        return def()
                .status(200)
                .noContent();
    }

    public static Builder ok(Object content, ObjectMapper objectMapper) {
        return ok()
                .body(content, objectMapper)
                .contentType("application/json");
    }

    public static Builder created(String location) {
        return def()
                .status(201)
                .noContent()
                .location(location);
    }

    public static Builder notFound() {
        return def()
                .status(404)
                .noContent();
    }

    public static Builder notFound(Object content, ObjectMapper objectMapper) {
        return notFound()
                .body(content, objectMapper)
                .contentType("application/json");
    }

    public static Builder internalServerError() {
        return def()
                .status(500)
                .noContent();
    }

    public static Builder internalServerError(Object content, ObjectMapper objectMapper) {
        return internalServerError()
                .body(content, objectMapper)
                .contentType("application/json");
    }

    public static class Builder {

        private Integer status;

        private Map<String, Object> headers;

        private String body;

        private Boolean base64Encoded = false;

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder header(String name, Object value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(name, value);
            return this;
        }

        public Builder body(Object content, ObjectMapper objectMapper) {
            try {
                body = objectMapper.writeValueAsString(content);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to convert Pojo into Body(Json-String)", e);
            }
            return this;
        }

        public Builder noContent() {
            body = "";
            return this;
        }

        public Builder base64Encoded(boolean base64Encoded) {
            this.base64Encoded = base64Encoded;
            return this;
        }

        public Builder contentType(String value) {
            header("Content-Type", value);
            return this;
        }

        public Builder location(String value) {
            header("Location", value);
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setStatusCode(status);
            response.setBase64Encoded(base64Encoded);
            response.setHeaders(headers);
            response.setBody(body);
            return response;
        }
    }

}

