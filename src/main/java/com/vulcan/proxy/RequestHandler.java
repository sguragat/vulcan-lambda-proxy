package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.common.Utils;

/**
 * Created by sg on 13/07/2018.
 */
public abstract class RequestHandler implements com.amazonaws.services.lambda.runtime.RequestHandler<Request, Response> {

    protected abstract Routes getRoutes();

    @Override
    public Response handleRequest(Request input, Context context) {
//        context.getLogger().log("REQUEST: " + Utils.asJson(input, new ObjectMapper()));
        return getRoutes().handleRequest(input, context);
    }
}
