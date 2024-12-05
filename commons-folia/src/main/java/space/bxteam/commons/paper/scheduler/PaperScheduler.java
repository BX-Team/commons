package space.bxteam.commons.paper.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import space.bxteam.commons.folia.scheduler.FoliaScheduler;

public class PaperScheduler extends FoliaScheduler {
    public PaperScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isGlobalThread() {
        return Bukkit.getServer().isPrimaryThread();
    }
}