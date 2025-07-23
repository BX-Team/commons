package org.bxteam.commons.logger.appender;

import org.bxteam.commons.logger.LogEntry;

/**
 * Interface for appender that process log entries.
 */
public interface Appender {
    /**
     * Appends the given log entry.
     *
     * @param entry the log entry to append
     */
    void append(LogEntry entry);

    /**
     * Closes the appender and releases any resources.
     */
    void close();
}
