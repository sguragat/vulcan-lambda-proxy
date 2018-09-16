package com.vulcan.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.common.TypedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 15/07/2018.
 */
public class Request extends HashMap<String, Object> {

    public void enforceLowercaseHeaders() {
        /**
         * When first time getHeaders() is called, the original Map is
         * being replaced Headers class which naturally works only with lower-case
         * header values.
         */
        getHeaders();
    }

    @JsonIgnore
    public String getResource() {
        return (String) get("resource");
    }

    @JsonIgnore
    public String getPath() {
        return (String) get("path");
    }

    @JsonIgnore
    public void setPath(String path) {
        put("path", path);
    }

    @JsonIgnore
    public String getHttpMethod() {
        return (String) get("httpMethod");
    }

    @JsonIgnore
    public void setHttpMethod(String httpMethod) {
        put("httpMethod", httpMethod);
    }

    @JsonIgnore
    public Headers getHeaders() {
        Map<String, Object> map = (Map<String, Object>) get("headers");
        if (map == null || (!(map instanceof Headers))) {
            map = (map == null) ? new Headers() : new Headers(map);
            put("headers", map);
        }
        return (Headers) map;
    }

    private TypedMap getOrCreateTypedMap(String key) {
        Map<String, Object> map = (Map<String, Object>) get(key);
        if (map == null || (!(map instanceof TypedMap))) {
            map = (map == null) ? new TypedMap() : new TypedMap(map);
            put(key, map);
        }
        return (TypedMap) map;
    }

    @JsonIgnore
    public TypedMap getQueryStringParameters() {
        return getOrCreateTypedMap("queryStringParameters");
    }

    @JsonIgnore
    public TypedMap getPathParameters() {
        return getOrCreateTypedMap("pathParameters");
    }

    @JsonIgnore
    public TypedMap getStageVariables() {
        return getOrCreateTypedMap("stageVariables");
    }

    @JsonIgnore
    public RequestContext getRequestContext() {
        return new RequestContext((Map<String, Object>) get("requestContext"));
    }

    @JsonIgnore
    public String getBody() {
        return (String) get("body");
    }

    @JsonIgnore
    public void setBody(String body) {
        put("body", body);
    }

    @JsonIgnore
    public <E> E getEntity(Class<E> entityType, ObjectMapper objectMapper) {
        return RequestBody.getEntity(this, entityType, objectMapper);
    }

    @JsonIgnore
    public <E> List<E> getEntityList(Class<E> entityType, ObjectMapper objectMapper) {
        return RequestBody.getJsonEntityList(this, entityType, objectMapper);
    }

    @JsonIgnore
    public boolean isBase64Encoded() {
        return (boolean) get("getBase64Encoded");
    }

    public static class RequestContext {

        private Map<String, Object> data;

        private RequestContext(Map<String, Object> data) {
            this.data = data;
        }

        public String getPath() {
            return (String) data.get("path");
        }

        public String getAccountId() {
            return (String) data.get("accountId");
        }

        public String getResourceId() {
            return (String) data.get("resourceId");
        }

        public String getStage() {
            return (String) data.get("stage");
        }

        public String getRequestId() {
            return (String) data.get("requestId");
        }

        public RequestIdentity getIdentity() {
            return new RequestIdentity((Map<String, Object>) data.get("identity"));
        }

        public String getResourcePath() {
            return (String) data.get("resourcePath");
        }

        public String getHttpMethod() {
            return (String) data.get("httpMethod");
        }

        public String getExtendedRequestId() {
            return (String) data.get("extendedRequestId");
        }

        public String getApiId() {
            return (String) data.get("apiId");
        }
    }

    private static class RequestIdentity {

        private Map<String, Object> data;

        public RequestIdentity(Map<String, Object> data) {
            this.data = data;
        }

        public String getCognitoIdentityPoolId() {
            return (String) data.get("cognitoIdentityPoolId");
        }

        public String getCognitoIdentityId() {
            return (String) data.get("cognitoIdentityId");
        }

        public String getApiKey() {
            return (String) data.get("apiKey");
        }

        public String getCognitoAuthenticationType() {
            return (String) data.get("cognitoAuthenticationType");
        }

        public String getUserArn() {
            return (String) data.get("userArn");
        }

        public String getApiKeyId() {
            return (String) data.get("apiKeyId");
        }

        public String getUserAgent() {
            return (String) data.get("userAgent");
        }

        public String getAccountId() {
            return (String) data.get("accountId");
        }

        public String getCaller() {
            return (String) data.get("caller");
        }

        public String getSourceIp() {
            return (String) data.get("sourceIp");
        }

        public String getAccessKey() {
            return (String) data.get("accessKey");
        }

        public String getCognitoAuthenticationProvider() {
            return (String) data.get("cognitoAuthenticationProvider");
        }

        public String getUser() {
            return (String) data.get("user");
        }
    }
}

