package org.bxteam.commons.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bxteam.commons.scheduler.Scheduler;
import org.bxteam.commons.scheduler.Task;

public class BukkitScheduler implements Scheduler {
    final Plugin plugin;

    public BukkitScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isGlobalThread() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isRegionThread(Location location) {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public Task runTask(Runnable runnable) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    @Override
    public Task runTaskLater(Runnable runnable, long delay) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
    }

    @Override
    public Task runTaskTimer(Runnable runnable, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public Task runTaskAsynchronously(Runnable runnable) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    @Override
    public Task runTaskLaterAsynchronously(Runnable runnable, long delay) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    @Override
    public Task runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    @Override
    public void execute(Runnable runnable) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable);
    }

    @Override
    public void cancelTasks() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
