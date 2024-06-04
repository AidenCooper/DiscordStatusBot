package net.arcdevs.discordstatusbot.dependency;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class DependencyChecker {
    @NotNull private final JavaPlugin plugin;

    protected EnumSet<DependencyType> loadedDependencies = EnumSet.noneOf(DependencyType.class);

    private Listener listener;

    public DependencyChecker(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled(@NotNull final DependencyType type) {
        return this.loadedDependencies.contains(type);
    }

    public void registerListener() {
        this.unregisterListener();
        this.listener = new DependencyListener(this, this.plugin);

        this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(listener);
    }
}
