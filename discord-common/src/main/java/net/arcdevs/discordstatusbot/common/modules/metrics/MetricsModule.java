package net.arcdevs.discordstatusbot.common.modules.metrics;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;
import org.jetbrains.annotations.NotNull;

@Getter(AccessLevel.PROTECTED)
public abstract class MetricsModule<T> extends DiscordModule {
    private final T metrics;

    public MetricsModule(@NotNull final T metrics) {
        this.metrics =  metrics;
    }
}
