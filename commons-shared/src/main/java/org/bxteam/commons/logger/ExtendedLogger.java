package org.bxteam.commons.logger;

import org.bxteam.commons.logger.appender.Appender;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Extended logger that provides convenient methods for logging messages at different levels.
 */
public class ExtendedLogger extends Logger {
    /**
     * Constructs an ExtendedLogger with the specified name.
     *
     * @param name the name of the logger
     */
    public ExtendedLogger(String name) {
        super(name);
    }

    /**
     * Constructs an ExtendedLogger with the specified parameters.
     *
     * @param name         the name of the logger
     * @param currentLevel the current log level
     * @param appenders    the list of appenders
     * @param listeners    the list of log entry listeners
     */
    public ExtendedLogger(String name, LogLevel currentLevel, List<Appender> appenders, List<Function<LogEntry, Boolean>> listeners) {
        super(name, currentLevel, appenders, listeners);
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param logLevel the log level
     * @param message  the message text
     */
    public void log(LogLevel logLevel, String message) {
        LogEntry entry = new LogEntry(
                this.name,
                logLevel,
                message,
                System.currentTimeMillis(),
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                null,
                new HashMap<>()
        );
        super.log(logLevel, entry, defaultAppenders);
    }

    /**
     * Logs a message along with a throwable.
     *
     * @param logLevel  the log level
     * @param message   the message text
     * @param throwable the throwable to log
     */
    public void log(LogLevel logLevel, String message, Throwable throwable) {
        LogEntry entry = new LogEntry(
                this.name,
                logLevel,
                message,
                System.currentTimeMillis(),
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                throwable,
                new HashMap<>()
        );
        super.log(logLevel, entry, defaultAppenders);
    }

    /**
     * Logs a throwable. The message is taken from the throwable's message.
     *
     * @param logLevel  the log level
     * @param throwable the throwable to log
     */
    public void log(LogLevel logLevel, Throwable throwable) {
        LogEntry entry = new LogEntry(
                this.name,
                logLevel,
                throwable.getMessage(),
                System.currentTimeMillis(),
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                throwable,
                new HashMap<>()
        );
        super.log(logLevel, entry, defaultAppenders);
    }

    /**
     * Logs a message at the DEBUG level.
     *
     * @param message the message text
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs a message at the INFO level.
     *
     * @param message the message text
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message at the WARN level.
     *
     * @param message the message text
     */
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    /**
     * Logs a throwable at the WARN level.
     *
     * @param throwable the throwable to log
     */
    public void warn(Throwable throwable) {
        log(LogLevel.WARN, throwable);
    }

    /**
     * Logs a message at the ERROR level.
     *
     * @param message the message text
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Logs a throwable at the ERROR level.
     *
     * @param throwable the throwable to log
     */
    public void error(Throwable throwable) {
        log(LogLevel.ERROR, throwable);
    }
}
