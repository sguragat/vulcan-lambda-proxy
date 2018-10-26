package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.Context;
import com.vulcan.converter.JSONObjectConverter;
import com.vulcan.converter.JsonConverter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by sg on 20/08/2018.
 */
public class Logger {

    private static final JsonConverter DEFAULT_JSON_CONVERTER = new JSONObjectConverter();

    private static final Object[] NO_ARGS = new Object[0];

    private static final Exception NO_EXCEPTION = null;

    private Level level;

    private Context context;

    Logger(Context context, String level) {
        this(context, Level.valueOf(level));
    }

    Logger(Context context, Level level) {
        this.context = context;
        this.level = level;
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
            } else if (sb.charAt(characterIndex) == '{' && sb.charAt(nextCharacterIndex) == 'O' && sb.charAt(nextNextCharacterIndex) == '}') { // placeholder found
                // check too many placeholders
                if (++placeholderCount > args.length) {
                    throw new IllegalStateException(String.format("Too many placeholders, but not enough args ( template = %s, args-count = %s )", template, args.length));
                }
                // replace placeholder with argument
                Object argument = args[argumentIndex++]; // read current arg & move index to next arg

                String sArgument = (argument != null) ? DEFAULT_JSON_CONVERTER.objectToJson(argument) : "null";
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

    public static void error(String message, Object... args) {
        error(NO_EXCEPTION, message, args);
    }

    public static void error(String message, Exception e) {
        error(e, message, NO_ARGS);
    }

    public static void error(Exception e, String message, Object... args) {
        THREAD_LOCAL_LOGGER.get().log(Level.ERROR, e, message, args);
    }

    public static void warn(String message, Object... args) {
        warn(NO_EXCEPTION, message, args);
    }

    public static void warn(String message, Exception e) {
        warn(e, message, NO_ARGS);
    }

    public static void warn(Exception e, String message, Object... args) {
        THREAD_LOCAL_LOGGER.get().log(Level.WARN, e, message, args);
    }

    public static void info(String message, Object... args) {
        info(NO_EXCEPTION, message, args);
    }

    public static void info(String message, Exception e) {
        info(e, message, NO_ARGS);
    }

    public static void info(Exception e, String message, Object... args) {
        THREAD_LOCAL_LOGGER.get().log(Level.INFO, e, message, args);
    }

    public static void debug(String message, Object... args) {
        debug(NO_EXCEPTION, message, args);
    }

    public static void debug(String message, Exception e) {
        debug(e, message, NO_ARGS);
    }

    public static void debug(Exception e, String message, Object... args) {
        THREAD_LOCAL_LOGGER.get().log(Level.DEBUG, e, message, args);
    }

    public static void detach() {
        THREAD_LOCAL_LOGGER.set(null);
    }

    enum Level {
        // be careful here as the order matters, see isLevelEnabled(..) method
        ERROR, WARN, INFO, DEBUG
    }
}
