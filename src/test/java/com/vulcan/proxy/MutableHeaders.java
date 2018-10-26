package com.vulcan.proxy;

/**
 * Created by sg on 26/10/2018.
 */
public class MutableHeaders extends Headers {

    public void accept(String value) {
        put(ACCEPT, value);
    }

    public void contentType(String value) {
        put(CONTENT_TYPE, value);
    }

    public void cacheControl(String value) {
        put(CACHE_CONTROL, value);
    }

    public void host(String value) {
        put(HOST, value);
    }

    public void userAgent(String value) {
        put(USER_AGENT, value);
    }

    public void authorization(String value) {
        put(AUTHORIZATION, value);
    }
}
