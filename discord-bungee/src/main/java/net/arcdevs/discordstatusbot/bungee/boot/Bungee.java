package net.arcdevs.discordstatusbot.bungee.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.BungeePlugin;
import net.arcdevs.discordstatusbot.bungee.dependency.BungeeDependency;
import net.arcdevs.discordstatusbot.bungee.logger.BungeeLogger;
import net.arcdevs.discordstatusbot.bungee.modules.command.BungeeCommandModule;
import net.arcdevs.discordstatusbot.bungee.modules.metrics.BungeeMetricsModule;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bungee.BungeeCommandHandler;

import java.io.File;
import java.util.logging.Handler;

@Getter
public class Bungee extends Discord {
    public static Bungee INSTANCE;

    @NotNull private final BungeePlugin plugin;

    public Bungee(@NotNull final BungeePlugin plugin) {
        Bungee.INSTANCE = this;

        this.plugin = plugin;
    }

    @Override
    public @NotNull DiscordDependency getDependency() {
        return new BungeeDependency();
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDataFolder();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new BungeeLogger(this.getPlugin().getLogger());
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.BUNGEE;
    }

    @Override
    public void enable() {
        super.enable();

        BungeeCommandHandler commandHandler = BungeeCommandHandler.create(this.getPlugin());
        Metrics metrics = new Metrics(this.getPlugin(), this.getPlatform().getMetricsID());

        this.getModuleManager().add(BungeeCommandModule.class, new BungeeCommandModule(commandHandler));
        this.getModuleManager().add(BungeeMetricsModule.class, new BungeeMetricsModule(metrics));
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void reload() {
        super.reload();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void shutdown() {
        super.shutdown();

        try {
            this.getPlugin().onDisable();

            for(Handler handler : this.getPlugin().getLogger().getHandlers()) handler.close();

            this.getPlugin().getProxy().getScheduler().cancel(this.getPlugin());
            this.getPlugin().getExecutorService().shutdownNow();
        } catch (Throwable throwable) {
            this.getLogger().severe("Exception disabling plugin " + this.getPlugin().getDescription().getName());
        }
    }
}
