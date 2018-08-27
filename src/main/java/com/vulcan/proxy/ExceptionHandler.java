package com.vulcan.proxy;

/**
 * Created by sg on 27/08/2018.
 */
public interface ExceptionHandler {

    Response handle(Exception e, Context context);

    class ExceptionHandlerImpl implements ExceptionHandler {

        @Override
        public Response handle(Exception e, Context context) {
            context.getLogger().error(e, "Request failed");
            return Response.internalServerError().build();
        }
    }

    ExceptionHandler INSTANCE = new ExceptionHandlerImpl();
}
