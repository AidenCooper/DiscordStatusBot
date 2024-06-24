package net.arcdevs.discordstatusbot.velocity.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.velocity.VelocityPlugin;
import net.arcdevs.discordstatusbot.velocity.logger.VelocityLogger;
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
        return new VelocityLogger(this.getPlugin().getLogger());
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
