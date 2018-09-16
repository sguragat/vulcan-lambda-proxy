package com.vulcan.proxy;

import com.vulcan.common.Utils;

/**
 * Created by sg on 27/08/2018.
 */
public interface ExceptionHandler {

    Response handle(Exception e, Context context);

    class ExceptionHandlerImpl implements ExceptionHandler {

        @Override
        public Response handle(Exception e, Context context) {
            Logger.error(e, "Request failed (500)");
            return Response.internalServerError("Internal server error", Utils.OBJECT_MAPPER).build();
        }
    }

    ExceptionHandler INSTANCE = new ExceptionHandlerImpl();
}
