package net.arcdevs.discordstatusbot.velocity.modules.metrics;

import net.arcdevs.discordstatusbot.common.modules.metrics.MetricsModule;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;

public class VelocityMetricsModule extends MetricsModule<Metrics> {
    public VelocityMetricsModule(@NotNull final Metrics metrics) {
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
