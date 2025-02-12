package org.bxteam.commons.scheduler;

import org.bukkit.plugin.Plugin;

public interface Task {
    /**
     * Cancels executing task
     */
    void cancel();

    /**
     * @return true if task is cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * @return true if task is currently executing, false otherwise
     */
    boolean isCurrentlyRunning();

    /**
     * @return true if task is repeating, false otherwise
     */
    boolean isRepeatingTask();

    /**
     * @return The plugin under which the task was scheduled.
     */
    Plugin getPlugin();
}
