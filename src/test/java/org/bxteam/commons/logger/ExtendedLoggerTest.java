package org.bxteam.commons.logger;

import org.bxteam.commons.logger.appender.Appender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExtendedLoggerTest {
    static class TestAppender implements Appender {
        final List<LogEntry> logEntries = new ArrayList<>();

        @Override
        public void append(LogEntry entry) {
            logEntries.add(entry);
        }

        @Override
        public void close() {
            // No resources to close.
        }
    }

    @Test
    public void testExtendedLoggerLoggingLevels() throws InterruptedException {
        TestAppender testAppender = new TestAppender();
        ExtendedLogger logger = new ExtendedLogger("TestLogger");
        logger.setCurrentLevel(LogLevel.INFO);
        logger.addAppender(testAppender);

        logger.log(LogLevel.DEBUG, "This is a debug message.");
        logger.log(LogLevel.INFO, "This is an informational message.");
        logger.log(LogLevel.WARN, "This is a warning message.");
        logger.log(LogLevel.ERROR, "This is an error message.");
        try {
            throw new RuntimeException("This is a test exception.");
        } catch (RuntimeException e) {
            logger.log(LogLevel.ERROR, e);
        }

        Thread.sleep(300);

        Assertions.assertEquals(4, testAppender.logEntries.size(), "Expected 4 log entries");

        LogEntry infoEntry = testAppender.logEntries.get(0);
        Assertions.assertEquals("This is an informational message.", infoEntry.message(), "INFO message mismatch");

        LogEntry warnEntry = testAppender.logEntries.get(1);
        Assertions.assertEquals("This is a warning message.", warnEntry.message(), "WARN message mismatch");

        LogEntry errorMessageEntry = testAppender.logEntries.get(2);
        Assertions.assertEquals("This is an error message.", errorMessageEntry.message(), "ERROR message mismatch");

        LogEntry errorExceptionEntry = testAppender.logEntries.get(3);
        Assertions.assertEquals("This is a test exception.", errorExceptionEntry.message(), "ERROR exception message mismatch");
    }
}
