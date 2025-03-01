package org.bxteam.commons.logger;

import org.bxteam.commons.logger.appender.Appender;
import org.bxteam.commons.logger.appender.ConsoleAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A simple asynchronous logger that supports multiple appenders and listeners.
 */
public class Logger {
    private static final Logger GLOBAL_LOGGER = new Logger("Commons-Global");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Commons-Logger");
        t.setDaemon(true);
        return t;
    });

    protected final String name;
    protected final List<Appender> defaultAppenders;
    protected final List<Function<LogEntry, Boolean>> listeners;
    protected LogLevel currentLevel;

    /**
     * Constructs a Logger with the specified name.
     * The default log level is INFO and a ConsoleAppender is added.
     *
     * @param name the name of the logger
     */
    public Logger(String name) {
        this.name = name;
        this.currentLevel = LogLevel.INFO;
        this.defaultAppenders = new ArrayList<>();
        this.defaultAppenders.add(new ConsoleAppender());
        this.listeners = new ArrayList<>();
    }

    /**
     * Constructs a Logger with the specified parameters.
     *
     * @param name         the name of the logger
     * @param currentLevel the current log level
     * @param appenders    the list of appenders
     * @param listeners    the list of log entry listeners
     */
    public Logger(String name, LogLevel currentLevel, List<Appender> appenders, List<Function<LogEntry, Boolean>> listeners) {
        this.name = name;
        this.currentLevel = currentLevel;
        this.defaultAppenders = new ArrayList<>(appenders);
        this.listeners = new ArrayList<>(listeners);
    }

    /**
     * Returns the global logger instance.
     *
     * @return the global Logger instance
     */
    public static Logger getGlobalLogger() {
        return GLOBAL_LOGGER;
    }

    /**
     * Logs the provided log entry using the specified log level and appenders.
     * The log entry is first processed by the listeners. If any listener returns false,
     * the logging is aborted.
     *
     * @param logLevel  the log level
     * @param logEntry  the log entry to log
     * @param appenders the list of appenders to use
     */
    public void log(LogLevel logLevel, LogEntry logEntry, List<Appender> appenders) {
        for (Function<LogEntry, Boolean> listener : listeners) {
            if (!listener.apply(logEntry)) {
                return;
            }
        }
        if (logLevel.ordinal() < currentLevel.ordinal()) {
            return;
        }
        executor.submit(() -> {
            try {
                for (Appender appender : appenders) {
                    appender.append(logEntry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Returns the name of the logger.
     *
     * @return the logger name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a new appender.
     *
     * @param appender the appender to add
     */
    public void addAppender(Appender appender) {
        this.defaultAppenders.add(appender);
    }

    /**
     * Adds a listener to filter or process log entries.
     *
     * @param listener the log entry listener
     */
    public void addListener(Function<LogEntry, Boolean> listener) {
        this.listeners.add(listener);
    }

    /**
     * Sets the current log level.
     *
     * @param currentLevel the new log level
     */
    public void setCurrentLevel(LogLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5L, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));
    }
}
