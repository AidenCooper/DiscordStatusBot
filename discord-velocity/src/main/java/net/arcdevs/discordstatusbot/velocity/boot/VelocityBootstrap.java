package net.arcdevs.discordstatusbot.velocity.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.boot.DiscordBootstrap;
import net.arcdevs.discordstatusbot.velocity.VelocityPlugin;
import net.arcdevs.discordstatusbot.velocity.logger.VelocityLogger;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;

@Getter
public class VelocityBootstrap extends DiscordBootstrap<VelocityPlugin> {
    private Metrics metrics;

    public VelocityBootstrap(@NotNull final VelocityPlugin plugin) {
        super(plugin, DiscordPlatform.VELOCITY, new VelocityLogger(plugin.getLogger()), plugin.getDirectory().toFile());
    }

    @Override
    public void enable() {
        // Metrics
        this.metrics = this.getPlugin().getMetricsFactory().make(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    public void disable() {
        if(this.getMetrics() != null) this.getMetrics().shutdown();

        this.getPlugin().getServer().shutdown();
    }

    @Override
    public void reload() {

    }
}
