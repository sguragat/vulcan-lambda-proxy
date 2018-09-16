package com.vulcan.proxy;

import com.vulcan.common.TypedMap;

import java.util.Map;

/**
 * Created by sg on 31/07/2018.
 */
public class Headers extends TypedMap {

    public static final String CONTENT_TYPE = "content-type";

    public static final String ACCEPT = "accept";

    public static final String CACHE_CONTROL = "cache-control";

    public static final String HOST = "host";

    public static final String USER_AGENT = "user-agent";

    public static final String AUTHORIZATION = "authorization";

    public Headers() {

    }

    public Headers(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public String accept() {
        return (String) get(ACCEPT);
    }

    public String contentType() {
        return (String) get(CONTENT_TYPE);
    }

    public String cacheControl() {
        return (String) get(CACHE_CONTROL);
    }

    public String host() {
        return (String) get(HOST);
    }

    public String userAgent() {
        return (String) get(USER_AGENT);
    }

    public String authorization() {
        return (String) get(AUTHORIZATION);
    }
}
