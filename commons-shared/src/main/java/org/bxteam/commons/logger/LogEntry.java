package org.bxteam.commons.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a log entry containing all information about a logging event.
 */
public record LogEntry(
        @NotNull String loggerName,
        @NotNull LogLevel logLevel,
        @NotNull String message,
        long timestamp,
        long threadID,
        @NotNull String threadName,
        @Nullable Throwable throwable,
        @NotNull Map<String, Object> properties
) {
    /**
     * Adds a property to the log entry.
     *
     * @param key   the property key
     * @param value the property value
     * @return the current LogEntry with the new property added
     */
    public LogEntry addProperty(@NotNull String key, @NotNull Object value) {
        this.properties.put(key, value);
        return this;
    }
}
