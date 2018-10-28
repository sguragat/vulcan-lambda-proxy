package com.vulcan.common;

/**
 * Created by sg on 28/10/2018.
 */
public class Envars {

    private Envars() {

    }

    public static boolean getPrettyPrint() {
        String sPretty = System.getenv("JSON_PRETTY_PRINT");
        if (sPretty == null) {
            return false;
        }
        return Boolean.valueOf(sPretty);
    }

    public static String getLogLevel() {
        String env = System.getenv("VULCAN_LOG_LEVEL");
        if (env == null) {
            return "ERROR";
        }
        return env;
    }
}
