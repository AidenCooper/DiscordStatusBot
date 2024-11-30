package net.arcdevs.discordstatusbot.bukkit.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bukkit.BukkitPlugin;
import net.arcdevs.discordstatusbot.bukkit.dependency.BukkitDependency;
import net.arcdevs.discordstatusbot.bukkit.logger.BukkitLogger;
import net.arcdevs.discordstatusbot.bukkit.modules.command.BukkitCommandModule;
import net.arcdevs.discordstatusbot.bukkit.modules.metrics.BukkitMetricsModule;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;

@Getter
public class Bukkit extends Discord {
    public static Bukkit INSTANCE;

    @NotNull private final BukkitPlugin plugin;

    public Bukkit(@NotNull final BukkitPlugin plugin) {
        Bukkit.INSTANCE = this;

        this.plugin = plugin;
    }

    @Override
    public @NotNull DiscordDependency getDependency() {
        return new BukkitDependency();
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDataFolder();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new BukkitLogger(this.getPlugin().getLogger());
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.BUKKIT;
    }

    @Override
    public void enable() {
        super.enable();

        // Configure modules
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this.getPlugin());
        commandHandler.enableAdventure();
        commandHandler.registerBrigadier();
        Metrics metrics = new Metrics(this.getPlugin(), this.getPlatform().getMetricsID());

        // Add modules
        this.getModuleManager().add(BukkitCommandModule.class, new BukkitCommandModule(commandHandler));
        this.getModuleManager().add(BukkitMetricsModule.class, new BukkitMetricsModule(metrics));
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public void shutdown() {
        super.shutdown();

        this.getPlugin().getPluginLoader().disablePlugin(this.getPlugin());
    }
}
