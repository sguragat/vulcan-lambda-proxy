package com.vulcan.proxy;

/**
 * Created by sg on 26/10/2018.
 */
public class RequestSample {

    private RequestSample() {

    }

    public static Request jsonRequest(String body) {
        Request request = new Request();

        MutableHeaders headers = new MutableHeaders();
        headers.contentType("application/json");

        request.put("headers", headers);
        request.put("body", body);

        return request;
    }

    public static Request formRequest(String body) {
        Request request = new Request();

        MutableHeaders headers = new MutableHeaders();
        headers.contentType("application/x-www-form-urlencoded");

        request.put("headers", headers);
        request.put("body", body);

        return request;
    }
}
