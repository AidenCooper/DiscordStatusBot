package net.arcdevs.discordstatusbot.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.arcdevs.test.BuildConstants;
import net.kyori.adventure.text.Component;
import net.william278.papiproxybridge.api.PlaceholderAPI;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Plugin(id = "discordstatusbot", name = "Discord Status Bot", version = BuildConstants.VERSION, description = BuildConstants.DESCRIPTION, dependencies = { @Dependency(id = "papiproxybridge", optional = true) })
public final class DiscordVelocityPlugin {
    @NotNull private final ProxyServer server;
    @NotNull private final Logger logger;
    @NotNull private final Path directory;
    @NotNull private final Metrics.Factory metricsFactory;

    private DiscordVelocity bootstrap;

    @Inject
    public DiscordVelocityPlugin(@NotNull final ProxyServer server, @NotNull final Logger logger, @NotNull @DataDirectory final Path directory, @NotNull Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.directory = directory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialize(@NotNull final ProxyInitializeEvent event) {
        this.bootstrap = new DiscordVelocity(this);
        this.bootstrap.initialize();
    }

    @Subscribe
    public void onProxyShutdown(@NotNull final ProxyShutdownEvent event) {
        this.bootstrap.shutdown();
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        final PlaceholderAPI placeholderAPI = PlaceholderAPI.createInstance();
        final Player player = event.getPlayer();

        placeholderAPI.formatPlaceholders("Hello %player_name%!", player.getUniqueId()).thenAccept(message -> {
            player.sendMessage(Component.text(message));
        });
    }
}
