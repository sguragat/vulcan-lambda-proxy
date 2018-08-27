package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.common.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by sg on 20/08/2018.
 */
public class Logger {

    private final static ObjectMapper OBJECT_MAPPER = Utils.OBJECT_MAPPER;

    private final static Object[] NO_ARGS = new Object[0];

    private final static Exception NO_EXCEPTION = null;

    private Level level;

    private Context context;

    public Logger(Context context, String level) {
        this(context, Level.valueOf(level));
    }

    public Logger(Context context, Level level) {
        this.context = context;
        this.level = level;
    }

    public void error(String message, Object... args) {
        error(NO_EXCEPTION, message, args);
    }

    public void error(String message, Exception e) {
        error(e, message, NO_ARGS);
    }

    public void error(Exception e, String message, Object... args) {
        log(Level.ERROR, e, message, args);
    }

    public void warn(String message, Object... args) {
        warn(NO_EXCEPTION, message, args);
    }

    public void warn(String message, Exception e) {
        warn(e, message, NO_ARGS);
    }

    public void warn(Exception e, String message, Object... args) {
        log(Level.WARN, e, message, args);
    }

    public void info(String message, Object... args) {
        info(NO_EXCEPTION, message, args);
    }

    public void info(String message, Exception e) {
        info(e, message, NO_ARGS);
    }

    public void info(Exception e, String message, Object... args) {
        log(Level.INFO, e, message, args);
    }

    public void debug(String message, Object... args) {
        debug(NO_EXCEPTION, message, args);
    }

    public void debug(String message, Exception e) {
        debug(e, message, NO_ARGS);
    }

    public void debug(Exception e, String message, Object... args) {
        log(Level.DEBUG, e, message, args);
    }

    private void log(Level level, Exception e, String message, Object... args) {
        if (isLevelEnabled(level)) {
            context.getLogger().log(replacePlaceHolders(level, message, args, e));
        }
    }

    private boolean isLevelEnabled(Level level) {
        return this.level.ordinal() >= level.ordinal();
    }

    static String replacePlaceHolders(Level level, String template, Object[] args) {
        return replacePlaceHolders(level, template, args, NO_EXCEPTION);
    }

    static String replacePlaceHolders(Level level, String template, Object[] args, Exception cause) {
        StringBuilder sb = new StringBuilder()
                .append(level.name())
                .append(" ").append(template);
        if (args.length == 0) {
            if (cause != null) {
                sb.append(System.lineSeparator()).append(getStackTrace(cause));
            }
            // if no arguments are passed, then return template as is with no change
            return sb.toString();
        }
        // at least one argument available, so replace placeholders
        int argumentIndex = 0;
        int characterIndex = 0;
        int nextCharacterIndex = characterIndex + 1;
        int nextNextCharacterIndex = nextCharacterIndex + 1;
        int placeholderCount = 0;
        while (characterIndex < sb.length()) {
            if (sb.charAt(characterIndex) == '{' && sb.charAt(nextCharacterIndex) == '}') { // placeholder found
                // check too many placeholders
                if (++placeholderCount > args.length) {
                    throw new IllegalStateException(String.format("Too many placeholders, but not enough args ( template = %s, args-count = %s )", template, args.length));
                }
                // replace placeholder with argument
                Object argument = args[argumentIndex++]; // read current arg & move index to next arg
                String sArgument = argument != null ? argument.toString() : "null";
                sb.replace(characterIndex, characterIndex + 2, sArgument);
                characterIndex += sArgument.length(); // fast forward to skip scanning argument value
                nextCharacterIndex = characterIndex + 1;
            } else if (sb.charAt(characterIndex) == '{' && sb.charAt(nextCharacterIndex) == 'o' && sb.charAt(nextNextCharacterIndex) == '}') { // placeholder found
                // check too many placeholders
                if (++placeholderCount > args.length) {
                    throw new IllegalStateException(String.format("Too many placeholders, but not enough args ( template = %s, args-count = %s )", template, args.length));
                }
                // replace placeholder with argument
                Object argument = args[argumentIndex++]; // read current arg & move index to next arg
                String sArgument = argument != null ? Utils.asJson(argument, OBJECT_MAPPER) : "null";
                sb.replace(characterIndex, characterIndex + 3, sArgument);
                characterIndex += sArgument.length(); // fast forward to skip scanning argument value
                nextCharacterIndex = characterIndex + 1;
                nextNextCharacterIndex = nextCharacterIndex + 1;
            }

            characterIndex++; // move to next character in the template
            nextCharacterIndex++;
            nextNextCharacterIndex++;
        }

        // check for too many args
        if (args.length > placeholderCount) {
            throw new IllegalStateException(String.format("Too many args, but not enough placeholders ( template = %s, args-count = %s )", template, args.length));
        }

        if (cause != null) {
            sb.append(System.lineSeparator()).append(getStackTrace(cause));
        }

        return sb.toString();
    }

    private static String getStackTrace(Exception e) {
        StringWriter stackTrace = new StringWriter();
        PrintWriter pw = new PrintWriter(stackTrace);
        e.printStackTrace(pw);
        pw.close();
        return stackTrace.toString();
    }

    private static final ThreadLocal<Logger> THREAD_LOCAL_LOGGER = new ThreadLocal<>();

    public static Logger init(Context context, String level) {
        return init(context, Level.valueOf(level));
    }

    public static Logger init(Context context, Level level) {
        Logger logger = new Logger(context, level);
        THREAD_LOCAL_LOGGER.set(logger);
        return logger;
    }

    public static Logger get() {
        return THREAD_LOCAL_LOGGER.get();
    }

    enum Level {
        // be careful here as the order matters, see isLevelEnabled(..) method
        ERROR, WARN, INFO, DEBUG
    }
}
