package com.vulcan.proxy;

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
        return new Builder()
                .contentType("application/json")
                .base64Encoded(false);
    }

    public static Builder ok() {
        return def()
                .status(200)
                .noContent();
    }

    public static Builder ok(Object body) {
        return ok()
                .body(body)
                .contentType("application/json");
    }

    public static Builder created() {
        return def()
                .status(201)
                .noContent();
    }

    public static Builder created(String location) {
        return created().location(location);
    }

    public static Builder created(Object body) {
        return created().body(body);
    }

    public static Builder created(Object body, String location) {
        return created(body).location(location);
    }

    public static Builder movedPermanently(String location) {
        return def()
                .status(301)
                .noContent()
                .location(location);
    }

    public static Builder found(String location) {
        return def()
                .status(302)
                .noContent()
                .location(location);
    }

    public static Builder seeOther(String location) {
        return def()
                .status(303)
                .noContent()
                .location(location);
    }

    public static Builder badRequest() {
        return def()
                .status(400)
                .noContent();
    }

    public static Builder badRequest(String message) {
        return badRequest(body(message));
    }

    private static Map<String, Object> body(String message) {
        Map<String, Object> content = new HashMap<>();
        content.put("message", message);
        return content;
    }

    public static Builder badRequest(Object body) {
        return badRequest().body(body);
    }

    public static Builder unauthorized() {
        return def()
                .status(401)
                .noContent();
    }

    public static Builder unauthorized(String message) {
        return unauthorized(body(message));
    }

    public static Builder unauthorized(Object body) {
        return unauthorized()
                .body(body)
                .contentType("application/json");
    }

    public static Builder notFound() {
        return def()
                .status(404)
                .noContent();
    }

    public static Builder notFound(String message) {
        return notFound(body(message));
    }

    public static Builder notFound(Object body) {
        return notFound()
                .body(body)
                .contentType("application/json");
    }

    public static Builder internalServerError() {
        return def()
                .status(500)
                .noContent();
    }

    public static Builder internalServerError(String message) {
        return internalServerError(body(message));
    }

    public static Builder internalServerError(Object body) {
        return internalServerError()
                .body(body)
                .contentType("application/json");
    }

    public static class Builder {

        private Integer status;

        private Map<String, Object> headers;

        private Object body;

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

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder noContent() {
            body = null;
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

        public Builder authenticationScheme(String authenticationScheme) {
            header("WWW-Authenticate", authenticationScheme);
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setStatusCode(status);
            response.setBase64Encoded(base64Encoded);
            response.setHeaders(headers);
            response.setBody("");
            return response;
        }

        public Response build(Context context) {
            Response response = build();
            if (body != null) {
                response.setBody(context.getBodyConverter().objectToJson(body));
            }
            return response;
        }
    }

}

