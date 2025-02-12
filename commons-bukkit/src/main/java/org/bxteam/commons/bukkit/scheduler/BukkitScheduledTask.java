package org.bxteam.commons.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bxteam.commons.scheduler.Task;

public class BukkitScheduledTask implements Task {
    BukkitTask task;
    boolean isRepeating;

    public BukkitScheduledTask(final BukkitTask task) {
        this.task = task;
        this.isRepeating = false;
    }

    public BukkitScheduledTask(final BukkitTask task, boolean isRepeating) {
        this.task = task;
        this.isRepeating = isRepeating;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public boolean isCurrentlyRunning() {
        // Fuck Bukkit, you don't have any other way
        return Bukkit.getServer().getScheduler().isCurrentlyRunning(this.task.getTaskId());
    }

    @Override
    public boolean isRepeatingTask() {
        return isRepeating;
    }

    @Override
    public Plugin getPlugin() {
        return task.getOwner();
    }
}
