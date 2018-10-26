package com.vulcan.proxy;

import com.vulcan.converter.BodyConverter;
import com.vulcan.converter.JSONObjectConverter;

/**
 * Created by sg on 13/07/2018.
 */
public abstract class RequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Request, Response> {

    private static final Logger.Level LOG_LEVEL = getLogLevel();

    private static final BodyConverter DEFAULT_BODY_CONVERTER = new JSONObjectConverter();

    protected abstract Routes getRoutes();

    protected abstract Object createContextState();

    protected BodyConverter getBodyConverter() {
        return DEFAULT_BODY_CONVERTER;
    }

    protected ExceptionHandler getExceptionHandler() {
        return ExceptionHandler.INSTANCE;
    }

    @Override
    public Response handleRequest(Request request, com.amazonaws.services.lambda.runtime.Context ctx) {
        Logger.init(ctx, LOG_LEVEL);
        Context context = new Context.Builder()
                .withRequest(request)
                .withLambdaContext(ctx)
                .withState(createContextState())
                .withBodyConverter(getBodyConverter())
                .build();

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

    private static Logger.Level getLogLevel() {
        String env = System.getenv("VULCAN_LOG_LEVEL");
        if (env == null) {
            return Logger.Level.ERROR;
        }
        return Logger.Level.valueOf(env);
    }
}
