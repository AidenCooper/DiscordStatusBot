package net.arcdevs.discordstatusbot.bukkit.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bukkit.BukkitPlugin;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordLogger;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Bukkit extends Discord {
    @NotNull private final BukkitPlugin plugin;

    private Metrics metrics;

    public Bukkit(@NotNull final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDataFolder();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new DiscordLogger() {
            @Override
            public void info(String message) {
                Bukkit.this.getLogger().info(message);
            }

            @Override
            public void warn(String message) {
                Bukkit.this.getLogger().warn(message);
            }

            @Override
            public void severe(String message) {
                Bukkit.this.getLogger().severe(message);
            }
        };
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.BUKKIT;
    }

    @Override
    protected void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    protected void disable() {
        if(this.getMetrics() != null) this.getMetrics().shutdown();
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void shutdown() {
        this.getPlugin().getPluginLoader().disablePlugin(this.getPlugin());
    }
}