package com.vulcan.proxy;

/**
 * Created by sg on 13/07/2018.
 */
public abstract class RequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Request, Response> {

    protected abstract Routes getRoutes();

    protected abstract Object createContextState();

    protected ExceptionHandler getExceptionHandler() {
        return ExceptionHandler.INSTANCE;
    }

    @Override
    public Response handleRequest(Request request, com.amazonaws.services.lambda.runtime.Context ctx) {
        Logger.init(ctx, getLevel());
        Context context = new Context(request, createContextState(), ctx);
        request.enforceLowercaseHeaders();
        Logger.debug("REQUEST: {O}", request);
        try {
            return getRoutes().handleRequest(context);
        } catch (Exception e) {
            return getExceptionHandler().handle(e, context);
        } finally {
            Logger.detach();
        }
    }

    private Logger.Level getLevel() {
        String env = System.getenv("VULCAN_LOG_LEVEL");
        if (env == null) {
            return Logger.Level.ERROR;
        }
        return Logger.Level.valueOf(env);
    }
}
