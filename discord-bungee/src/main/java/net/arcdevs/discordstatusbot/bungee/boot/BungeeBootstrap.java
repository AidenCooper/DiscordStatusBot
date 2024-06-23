package net.arcdevs.discordstatusbot.bungee.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.BungeePlugin;
import net.arcdevs.discordstatusbot.bungee.logger.BungeeLogger;
import net.arcdevs.discordstatusbot.common.boot.DiscordBootstrap;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;

@Getter
public class BungeeBootstrap extends DiscordBootstrap<BungeePlugin> {
    private Metrics metrics;

    public BungeeBootstrap(@NotNull final BungeePlugin plugin) {
        super(plugin, DiscordPlatform.BUNGEE, new BungeeLogger(plugin.getLogger()), plugin.getDataFolder());
    }

    @Override
    public void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    public void disable() {
        if(this.getMetrics() != null) this.getMetrics().shutdown();
    }

    @Override
    public void reload() {

    }
}
