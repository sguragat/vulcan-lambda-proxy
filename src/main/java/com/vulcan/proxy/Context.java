package com.vulcan.proxy;

/**
 * Created by sg on 21/08/2018.
 */
public class Context {

    private Logger logger;

    public Context(com.amazonaws.services.lambda.runtime.Context context) {
        logger = Logger.init(context, getLevel());
    }

    public Logger getLogger() {
        return logger;
    }

    private Logger.Level getLevel() {
        String env = System.getenv("VULCAN_LOG_LEVEL");
        if (env == null) {
            return Logger.Level.ERROR;
        }
        return Logger.Level.valueOf(env);
    }
}
