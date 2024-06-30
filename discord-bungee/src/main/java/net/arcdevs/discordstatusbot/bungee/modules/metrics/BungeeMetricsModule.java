package net.arcdevs.discordstatusbot.bungee.modules.metrics;

import net.arcdevs.discordstatusbot.common.modules.metrics.MetricsModule;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;

public class BungeeMetricsModule extends MetricsModule<Metrics> {
    public BungeeMetricsModule(@NotNull final Metrics metrics) {
        super(metrics);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {
        this.getMetrics().shutdown();
    }

    @Override
    protected void reload() {

    }
}
