package com.vulcan.proxy;

/**
 * Created by sg on 13/07/2018.
 */
public abstract class RequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Request, Response> {

    protected abstract Routes getRoutes();

    @Override
    public Response handleRequest(Request request, com.amazonaws.services.lambda.runtime.Context context) {
        return getRoutes().handleRequest(request, new Context(context));
    }

}
