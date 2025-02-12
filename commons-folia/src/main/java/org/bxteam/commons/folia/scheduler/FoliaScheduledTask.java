package org.bxteam.commons.folia.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bxteam.commons.scheduler.Task;

public class FoliaScheduledTask implements Task {
    private final ScheduledTask task;

    public FoliaScheduledTask(final ScheduledTask task) {
        this.task = task;
    }

    public void cancel() {
        this.task.cancel();
    }

    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    public Plugin getPlugin() {
        return this.task.getOwningPlugin();
    }

    public boolean isCurrentlyRunning() {
        final ScheduledTask.ExecutionState state = this.task.getExecutionState();
        return state == ScheduledTask.ExecutionState.RUNNING || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    public boolean isRepeatingTask() {
        return this.task.isRepeatingTask();
    }
}
