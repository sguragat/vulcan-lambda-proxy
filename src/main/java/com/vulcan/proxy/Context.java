package com.vulcan.proxy;

/**
 * Created by sg on 21/08/2018.
 */
public class Context {

    private final Request request;

    private final Object state;

    public Context(Request request, Object state, com.amazonaws.services.lambda.runtime.Context context) {
        this.request = request;
        this.state = state;
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
}
