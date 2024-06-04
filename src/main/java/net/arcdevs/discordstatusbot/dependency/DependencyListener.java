package net.arcdevs.discordstatusbot.dependency;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DependencyListener implements Listener {
    @NotNull private final DependencyChecker dependency;
    @NotNull private final JavaPlugin plugin;

    public DependencyListener(@NotNull final DependencyChecker dependency, @NotNull final JavaPlugin plugin) {
        this.dependency = dependency;
        this.plugin = plugin;

        for(DependencyType type : DependencyType.values()) {
            if(Bukkit.getPluginManager().getPlugin(type.getName()) != null) {
                this.enable(type);
            } else {
                this.disable(type);
            }
        }
    }

    @EventHandler
    public void onPluginEnable(@NotNull final PluginEnableEvent event) {
        for(DependencyType type : DependencyType.values()) {
            if(type.getName().equals(event.getPlugin().getName())) {
                this.enable(type);
            }
        }
    }

    @EventHandler
    public void onPluginDisable(@NotNull final PluginDisableEvent event) {
        for(DependencyType type : DependencyType.values()) {
            if(type.getName().equals(event.getPlugin().getName())) {
                this.disable(type);
            }
        }
    }

    private void enable(@NotNull final DependencyType type) {
        if(this.dependency.loadedDependencies.add(type))
            this.plugin.getLogger().info("Loaded " + type.getName() + ".");
    }

    private void disable(@NotNull final DependencyType type) {
        if(type.isRequired()) {
            this.plugin.getLogger().severe(type.getName() + " is required and could not be found.");
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
            return;
        }

        if(this.dependency.loadedDependencies.remove(type))
            this.plugin.getLogger().info("Unloaded " + type.getName() + ".");
    }
}
