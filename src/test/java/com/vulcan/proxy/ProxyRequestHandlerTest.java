package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.converter.BodyConverter;
import com.vulcan.converter.JacksonConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sg on 28/07/2018.
 */
public class ProxyRequestHandlerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private BodyConverter bodyConverter = new JacksonConverter(objectMapper, true);

    @Test
    public void handleRequest() {
        Request request = request();
        request.setHttpMethod("GET");
        request.setPath("/product/12345678");

        // method under test
        Response response = lambdaHandler().handleRequest(request, context());

        // assert
        Assert.assertEquals(200, response.getStatusCode().intValue());
    }

    private RequestHandler lambdaHandler() {
        Routes routes = new Routes.Builder()
                .get("/product/{id}", new DummyRoute())
                .build();

        return new RequestHandler() {

            @Override
            public Response handleRequest(Request request, Context context) {
                return Response.ok().build();
            }

            @Override
            protected Routes getRoutes() {
                return routes;
            }

            @Override
            protected Object createContextState() {
                return new Object();
            }

            @Override
            protected BodyConverter getBodyConverter() {
                return bodyConverter;
            }
        };
    }

    private Request request() {
        Request request = new Request();
        return request;
    }

    private Context context() {
        return new Context() {
            @Override
            public String getAwsRequestId() {
                return null;
            }

            @Override
            public String getLogGroupName() {
                return null;
            }

            @Override
            public String getLogStreamName() {
                return null;
            }

            @Override
            public String getFunctionName() {
                return null;
            }

            @Override
            public String getFunctionVersion() {
                return null;
            }

            @Override
            public String getInvokedFunctionArn() {
                return null;
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String message) {
                        System.out.println(message);
                    }

                    @Override
                    public void log(byte[] message) {
                        System.out.println(new String(message));
                    }
                };
            }
        };
    }
}
