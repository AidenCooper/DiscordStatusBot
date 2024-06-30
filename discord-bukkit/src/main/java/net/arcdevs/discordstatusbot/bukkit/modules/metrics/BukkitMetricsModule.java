package net.arcdevs.discordstatusbot.bukkit.modules.metrics;

import net.arcdevs.discordstatusbot.common.modules.metrics.MetricsModule;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

public class BukkitMetricsModule extends MetricsModule<Metrics> {
    public BukkitMetricsModule(@NotNull final Metrics metrics) {
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
