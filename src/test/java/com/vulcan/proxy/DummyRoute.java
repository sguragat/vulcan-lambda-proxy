package com.vulcan.proxy;

/**
 * Created by sg on 16/09/2018.
 */
public class DummyRoute implements Route {

    @Override
    public Response handleRequest(Context context) {
        return Response.ok().build();
    }
}
