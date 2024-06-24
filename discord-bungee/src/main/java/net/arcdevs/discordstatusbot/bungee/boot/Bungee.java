package net.arcdevs.discordstatusbot.bungee.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.BungeePlugin;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordLogger;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Handler;

@Getter
public class Bungee extends Discord {
    @NotNull private final BungeePlugin plugin;

    private Metrics metrics;

    public Bungee(@NotNull final BungeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDataFolder();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new DiscordLogger() {
            @Override
            public void info(String message) {
                Bungee.this.getLogger().info(message);
            }

            @Override
            public void warn(String message) {
                Bungee.this.getLogger().warn(message);
            }

            @Override
            public void severe(String message) {
                Bungee.this.getLogger().severe(message);
            }
        };
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.BUNGEE;
    }

    @Override
    protected void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    protected void disable() {
        if(this.getMetrics() != null) this.getMetrics().shutdown();
    }

    @Override
    protected void reload() {

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void shutdown() {
        try {
            this.getPlugin().onDisable();
            for(Handler handler : this.getPlugin().getLogger().getHandlers()) {
                handler.close();
            }
            this.getPlugin().getProxy().getScheduler().cancel(this.getPlugin());
            this.getPlugin().getExecutorService().shutdownNow();
        } catch (Throwable throwable) {
            this.getLogger().severe("Exception disabling plugin " + this.getPlugin().getDescription().getName());
        }
    }
}
