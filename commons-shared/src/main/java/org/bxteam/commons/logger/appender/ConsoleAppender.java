package org.bxteam.commons.logger.appender;

import org.bxteam.commons.logger.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

/**
 * Appender that outputs log entries to the console.
 */
public class ConsoleAppender implements Appender {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final PrintStream out;
    @NotNull
    private final String format;

    /**
     * Constructs a ConsoleAppender with the specified format.
     *
     * @param format the format string for log messages
     */
    public ConsoleAppender(@NotNull String format) {
        this.format = format;
        this.out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                System.out.write(b);
            }
        });
    }

    /**
     * Constructs a ConsoleAppender with a default format.
     */
    public ConsoleAppender() {
        this("[{loggerName}] {timestamp} {logLevel}: {message}");
    }

    /**
     * Appends the log entry to the console using the specified format.
     *
     * @param logEntry the log entry to append
     */
    @Override
    public void append(LogEntry logEntry) {
        String message = format
                .replace("{loggerName}", logEntry.loggerName())
                .replace("{timestamp}", sdf.format(logEntry.timestamp()))
                .replace("{threadName}", logEntry.threadName())
                .replace("{logLevel}", logEntry.logLevel().name())
                .replace("{message}", logEntry.message());

        if (logEntry.throwable() != null) {
            message += "\n" + logEntry.throwable();
        }

        out.println(message);
    }

    /**
     * Closes the appender. No resources to release in this implementation.
     */
    @Override
    public void close() {
        // No resources to close.
    }
}
