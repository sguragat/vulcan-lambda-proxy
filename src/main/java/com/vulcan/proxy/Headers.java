package com.vulcan.proxy;

import com.vulcan.common.TypedMap;

import java.util.Map;

/**
 * Created by sg on 31/07/2018.
 */
public class Headers extends TypedMap {

    public Headers() {

    }

    public Headers(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public String accept() {
        return (String) get("accept");
    }

    public String contentType() {
        return (String) get("content-type");
    }

    public String cacheControl() {
        return (String) get("cache-control");
    }

    public String host() {
        return (String) get("host");
    }

    public String userAgent() {
        return (String) get("user-agent");
    }

    public String authorization() {
        return (String) get("authorization");
    }
}
