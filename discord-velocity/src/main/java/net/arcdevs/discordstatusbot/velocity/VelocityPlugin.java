package net.arcdevs.discordstatusbot.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.velocity.boot.Velocity;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
public final class VelocityPlugin {
    @NotNull private final ProxyServer server;
    @NotNull private final Logger logger;
    @NotNull private final Path directory;
    @NotNull private final Metrics.Factory metricsFactory;

    @Setter(AccessLevel.PRIVATE) private Velocity velocity;

    @Inject
    public VelocityPlugin(@NotNull final ProxyServer server, @NotNull final Logger logger, @NotNull @DataDirectory final Path directory, @NotNull final Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.directory = directory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialize(@NotNull final ProxyInitializeEvent event) {
        this.setVelocity(new Velocity(this));
        this.getVelocity().enablePlugin();
    }

    @Subscribe
    public void onProxyShutdown(@NotNull final ProxyShutdownEvent event) {
        this.getVelocity().disablePlugin(false);
    }
}
