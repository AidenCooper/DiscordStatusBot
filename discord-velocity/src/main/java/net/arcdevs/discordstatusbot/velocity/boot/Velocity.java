package net.arcdevs.discordstatusbot.velocity.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.DiscordLogger;
import net.arcdevs.discordstatusbot.velocity.VelocityPlugin;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Velocity extends Discord {
    @NotNull private final VelocityPlugin plugin;

    private Metrics metrics;

    public Velocity(@NotNull final VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDirectory().toFile();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new DiscordLogger() {
            @Override
            public void info(String message) {
                Velocity.this.getPlugin().getLogger().info(message);
            }

            @Override
            public void warn(String message) {
                Velocity.this.getPlugin().getLogger().warn(message);
            }

            @Override
            public void severe(String message) {
                Velocity.this.getPlugin().getLogger().error(message);
            }
        };
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.VELOCITY;
    }

    @Override
    protected void enable() {
        // Metrics
        this.metrics = this.getPlugin().getMetricsFactory().make(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    protected void disable() {
        // Metrics
        if(this.getMetrics() != null) this.getMetrics().shutdown();
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void shutdown() {
        this.getPlugin().getServer().shutdown();
    }
}
