package com.vulcan.proxy;

/**
 * Created by sg on 13/07/2018.
 */
public abstract class RequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Request, Response> {

    protected abstract Routes getRoutes();

    protected ExceptionHandler getExceptionHandler() {
        return ExceptionHandler.INSTANCE;
    }

    @Override
    public Response handleRequest(Request request, com.amazonaws.services.lambda.runtime.Context c) {
        request.lowercaseHeaders();
        Context context = new Context(c);
        context.getLogger().debug("REQUEST: {O}", request);
        try {
            return getRoutes().handleRequest(request, context);
        } catch (Exception e) {
            return getExceptionHandler().handle(e, context);
        }
    }

}
