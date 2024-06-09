package net.arcdevs.discordstatusbot.bungee;

import net.arcdevs.discordstatusbot.api.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.DiscordBootstrap;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;

public class DiscordBungee extends DiscordBootstrap<DiscordBungeePlugin> {
    public static DiscordBungee INSTANCE;

    public DiscordBungee(@NotNull final DiscordBungeePlugin plugin) {
        super(plugin, DiscordPlatform.BUNGEE, plugin.getDataFolder());

        DiscordBungee.INSTANCE = this;
    }

    private Metrics metrics;

    @Override
    public void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());

        this.getPlugin().getLogger().info("Bungee enabled.");
    }

    @Override
    public void disable() {
        if(this.metrics != null) this.metrics.shutdown();

        this.getPlugin().getLogger().info("Bungee disabled.");
    }
}
