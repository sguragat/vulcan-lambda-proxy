package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.vulcan.proxy.Logger.Level.DEBUG;
import static com.vulcan.proxy.Logger.Level.INFO;
import static com.vulcan.proxy.Logger.replacePlaceHolders;

/**
 * Created by sg on 20/08/2018.
 */
public class LoggerTest {

    private static final boolean MESSAGE_LOGGED = true;
    private static final boolean MESSAGE_NOT_LOGGED = false;

    private List<String> logHistory;

    @Before
    public void setUp() {
        logHistory = new ArrayList<>();
    }

    @Test
    public void debug_when_level_is_DEBUG() {
        debug("DEBUG", MESSAGE_LOGGED);
    }

    @Test
    public void debug_when_level_is_INFO() {
        debug("INFO", MESSAGE_NOT_LOGGED);
    }

    @Test
    public void debug_when_level_is_WARN() {
        debug("WARN", MESSAGE_NOT_LOGGED);
    }

    @Test
    public void debug_when_level_is_ERROR() {
        debug("ERROR", MESSAGE_NOT_LOGGED);
    }

    private void debug(String level, boolean expected) {
        Logger logger = init(level);

        // call method under test
        logger.debug("Hey!");

        // assert
        Assert.assertEquals(expected, logHistory.contains("DEBUG Hey!"));
    }

    @Test
    public void info_when_level_is_DEBUG() {
        info("DEBUG", MESSAGE_LOGGED);
    }

    @Test
    public void info_when_level_is_INFO() {
        info("INFO", MESSAGE_LOGGED);
    }

    @Test
    public void info_when_level_is_WARN() {
        info("WARN", MESSAGE_NOT_LOGGED);
    }

    @Test
    public void info_when_level_is_ERROR() {
        info("ERROR", MESSAGE_NOT_LOGGED);
    }

    private void info(String level, boolean expected) {
        Logger logger = init(level);

        // call method under test
        logger.info("Hey!");

        // assert
        Assert.assertEquals(expected, logHistory.contains("INFO Hey!"));
    }

    @Test
    public void warn_when_level_is_DEBUG() {
        warn("DEBUG", MESSAGE_LOGGED);
    }

    @Test
    public void warn_when_level_is_INFO() {
        warn("INFO", MESSAGE_LOGGED);
    }

    @Test
    public void warn_when_level_is_WARN() {
        warn("WARN", MESSAGE_LOGGED);
    }

    @Test
    public void warn_when_level_is_ERROR() {
        warn("ERROR", MESSAGE_NOT_LOGGED);
    }

    private void warn(String level, boolean expected) {
        Logger logger = init(level);

        // call method under test
        logger.warn("Hey!");

        // assert
        Assert.assertEquals(expected, logHistory.contains("WARN Hey!"));
    }

    @Test
    public void error_when_level_is_DEBUG() {
        error("DEBUG", MESSAGE_LOGGED);
    }

    @Test
    public void error_when_level_is_INFO() {
        error("INFO", MESSAGE_LOGGED);
    }

    @Test
    public void error_when_level_is_WARN() {
        error("WARN", MESSAGE_LOGGED);
    }

    @Test
    public void error_when_level_is_ERROR() {
        error("ERROR", MESSAGE_LOGGED);
    }

    @Test
    public void replacePlaceHolders_when_1_placeholder() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hello {}!", new Object[] { "world" });

        // assert
        Assert.assertEquals("DEBUG Hello world!", message);
    }

    @Test
    public void replacePlaceHolders_when_2_placeholder() {
        // call method under test
        String message = replacePlaceHolders(INFO, "Hi {} {}", new Object[] { "Jack!", "How are you?" });

        // assert
        Assert.assertEquals("INFO Hi Jack! How are you?", message);
    }

    @Test
    public void replacePlaceHolders_when_starts_with_placeholder() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "{} Jack!", new Object[] { "Hi" });

        // assert
        Assert.assertEquals("DEBUG Hi Jack!", message);
    }

    @Test
    public void replacePlaceHolders_when_ends_with_placeholder() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi Jack{}", new Object[] { "!" });

        // assert
        Assert.assertEquals("DEBUG Hi Jack!", message);
    }

    @Test
    public void replacePlaceHolders_when_arguments_is_empty() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi{}, how are you?", new Object[] { "" });

        // assert
        Assert.assertEquals("DEBUG Hi, how are you?", message);
    }

    @Test
    public void replacePlaceHolders_when_arguments_is_null() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi {}, how are you?", new Object[] { null });

        // assert
        Assert.assertEquals("DEBUG Hi null, how are you?", message);
    }

    @Test
    public void replacePlaceHolders_when_arguments_of_different_types() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "{}, are you {}?", new Object[] { "Jack", 25 });

        // assert
        Assert.assertEquals("DEBUG Jack, are you 25?", message);
    }

    @Test
    public void replacePlaceHolders_when_no_placeholders() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi Jack!", new Object[] {});

        // assert
        Assert.assertEquals("DEBUG Hi Jack!", message);
    }

    @Test
    public void replacePlaceHolders_with_cause() {
        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hello {}!", new Object[] { "world" }, new TimeoutException("Timeout"));

        // assert
        Assert.assertEquals(true, message.startsWith("DEBUG Hello world!" + System.lineSeparator() + "java.util.concurrent.TimeoutException: Timeout" + System.lineSeparator() + '\t' + "at"));
    }

    @Test
    public void replacePlaceHolders_when_placeholders_more_than_arguments_should_throw() {
        // call method under test
        try {
            replacePlaceHolders(DEBUG, "Hi {} {}, how are you?", new Object[] { "Jack" });
            Assert.fail("Exception expected, but none thrown");
        } catch (IllegalStateException e) {
            // assert
            Assert.assertEquals(true, e.getMessage().startsWith("Too many placeholders, but not enough args"));
        }
    }

    @Test
    public void replacePlaceHolders_when_arguments_more_than_placeholders_should_throw() {
        // call method under test
        try {
            replacePlaceHolders(DEBUG, "Hi {}, how are you?", new Object[] { "Jack", "Nick" });
            Assert.fail("Exception expected, but none thrown");
        } catch (IllegalStateException e) {
            // assert
            Assert.assertEquals(true, e.getMessage().startsWith("Too many args, but not enough placeholders"));
        }
    }

    @Test
    public void replacePlaceHolders_with_1_json_argument() {
        Map<String, Object> pojo = new HashMap<>();
        pojo.put("message", "Hello");

        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi Jack! {o}", new Object[] { pojo });

        // assert
        Assert.assertEquals("DEBUG Hi Jack! {\"message\":\"Hello\"}", message);
    }

    @Test
    public void replacePlaceHolders_with_2_json_arguments() {
        Map<String, Object> pojo = new HashMap<>();
        pojo.put("message", "Hello");

        // call method under test
        String message = replacePlaceHolders(DEBUG, "Hi Jack! {o} {o}", new Object[] { pojo, pojo });

        // assert
        Assert.assertEquals("DEBUG Hi Jack! {\"message\":\"Hello\"} {\"message\":\"Hello\"}", message);
    }

    private void error(String level, boolean expected) {
        Logger logger = init(level);

        // call method under test
        logger.error("Hey!");

        // assert
        Assert.assertEquals(expected, logHistory.contains("ERROR Hey!"));
    }

    private Logger init(String level) {
        return Logger.init(mockContext(), level);
    }

    private Context mockContext() {
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
                        logHistory.add(message);
                        System.out.println(message);
                    }
                    @Override
                    public void log(byte[] message) {
                        this.log(new String(message));
                    }
                };
            }
        };
    }
}
