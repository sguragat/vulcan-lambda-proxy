package com.vulcan.common;

/**
 * Created by sg on 16/09/2018.
 */
public class ThreadContext {

    private final Object context;

    public ThreadContext(Object context) {
        this.context = context;
    }

    private static final ThreadLocal<ThreadContext> THREAD_LOCAL = new ThreadLocal<>();

    public static ThreadContext set(ThreadContext context) {
        THREAD_LOCAL.set(context);
        return context;
    }

    private static ThreadContext get() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.set(null);
    }

    public static <E> E context() {
        return (E) get().context;
    }
}
