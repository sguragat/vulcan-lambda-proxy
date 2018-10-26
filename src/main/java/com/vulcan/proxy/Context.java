package com.vulcan.proxy;

import com.vulcan.converter.BodyConverter;

/**
 * Created by sg on 21/08/2018.
 */
public class Context {

    private Request request;

    private Object state;

    private com.amazonaws.services.lambda.runtime.Context lambdaContext;

    private BodyConverter bodyConverter;

    private Context() {
    }

    public <E> E getState() {
        return (E) state;
    }

    public <E> E getState(Class<E> type) {
        return (E) state;
    }

    public Request getRequest() {
        return request;
    }

    public BodyConverter getBodyConverter() {
        return bodyConverter;
    }

    public com.amazonaws.services.lambda.runtime.Context getLambdaContext() {
        return lambdaContext;
    }

    public static class Builder {

        private Request request;

        private Object state;

        private BodyConverter bodyConverter;

        private com.amazonaws.services.lambda.runtime.Context lambdaContext;

        public Builder withRequest(Request request) {
            this.request = request;
            return this;
        }

        public Builder withState(Object state) {
            this.state = state;
            return this;
        }

        public Builder withBodyConverter(BodyConverter bodyConverter) {
            this.bodyConverter = bodyConverter;
            return this;
        }

        public Builder withLambdaContext(com.amazonaws.services.lambda.runtime.Context lambdaContext) {
            this.lambdaContext = lambdaContext;
            return this;
        }

        public Context build() {
            Context context = new Context();
            context.request = request;
            context.state = state;
            context.bodyConverter = bodyConverter;
            context.lambdaContext = lambdaContext;
            return context;
        }
    }
}
