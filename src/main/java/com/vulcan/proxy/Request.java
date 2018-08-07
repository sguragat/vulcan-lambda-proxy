package com.vulcan.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.common.TypedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 15/07/2018.
 */
public class Request extends HashMap<String, Object> {

    public String getResource() {
        return (String) get("resource");
    }

    public String getPath() {
        return (String) get("path");
    }

    public void setPath(String path) {
        put("path", path);
    }

    public String getHttpMethod() {
        return (String) get("httpMethod");
    }

    public void setHttpMethod(String httpMethod) {
        put("httpMethod", httpMethod);
    }

    public Headers getHeaders() {
        Map<String, Object> map = (Map<String, Object>) get("headers");
        if (map == null || (!(map instanceof Headers))) {
            map = (map == null) ? new Headers() : new Headers(map);
            put("headers", map);
        }
        return (Headers) map;
    }

    private TypedMap getOrCreate(String key) {
        Map<String, Object> map = (Map<String, Object>) get(key);
        if (map == null || (!(map instanceof TypedMap))) {
            map = new TypedMap();
            put(key, map);
        }
        return (TypedMap) map;
    }

    public TypedMap getQueryStringParameters() {
        return getOrCreate("queryStringParameters");
    }

    public TypedMap getPathParameters() {
        return getOrCreate("pathParameters");
    }

    public TypedMap getStageVariables() {
        return getOrCreate("stageVariables");
    }

    public RequestContext getRequestContext() {
        return new RequestContext((Map<String, Object>) get("requestContext"));
    }

    public String getBody() {
        return (String) get("body");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public <E> E getEntity(Class<E> entityType, ObjectMapper objectMapper) {
        return RequestBody.getJsonEntity(this, entityType, objectMapper);
    }

    public <E> List<E> getEntityList(Class<E> entityType, ObjectMapper objectMapper) {
        return RequestBody.getJsonEntityList(this, entityType, objectMapper);
    }

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

