package net.arcdevs.discordstatusbot.velocity;

import lombok.Getter;
import net.arcdevs.discordstatusbot.api.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.DiscordBootstrap;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;

@Getter
public class DiscordVelocity extends DiscordBootstrap<DiscordVelocityPlugin> {
    public static DiscordVelocity INSTANCE;

    public DiscordVelocity(@NotNull final DiscordVelocityPlugin plugin) {
        super(plugin, DiscordPlatform.VELOCITY, plugin.getDirectory().toFile());

        DiscordVelocity.INSTANCE = this;
    }

    private Metrics metrics;

    @Override
    public void enable() {
        this.metrics = this.getPlugin().getMetricsFactory().make(this.getPlugin(), this.getPlatform().getId());

        this.getPlugin().getLogger().info("Velocity enabled.");
    }

    @Override
    public void disable() {
        if(this.metrics != null) this.metrics.shutdown();

        this.getPlugin().getLogger().info("Velocity disabled.");
    }
}
