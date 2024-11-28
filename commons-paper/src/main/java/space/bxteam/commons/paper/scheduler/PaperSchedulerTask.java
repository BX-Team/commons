package space.bxteam.commons.paper.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import space.bxteam.commons.scheduler.Task;

public class PaperSchedulerTask implements Task {
    private final ScheduledTask task;

    public PaperSchedulerTask(final ScheduledTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public Plugin getPlugin() {
        return this.task.getOwningPlugin();
    }

    @Override
    public boolean isCurrentlyRunning() {
        final ScheduledTask.ExecutionState state = this.task.getExecutionState();
        return state == ScheduledTask.ExecutionState.RUNNING || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    @Override
    public boolean isRepeatingTask() {
        return this.task.isRepeatingTask();
    }
}
