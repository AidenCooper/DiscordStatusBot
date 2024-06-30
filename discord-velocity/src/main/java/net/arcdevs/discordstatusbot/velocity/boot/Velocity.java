package net.arcdevs.discordstatusbot.velocity.boot;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.velocity.VelocityPlugin;
import net.arcdevs.discordstatusbot.velocity.logger.VelocityLogger;
import net.arcdevs.discordstatusbot.velocity.modules.command.VelocityCommandModule;
import net.arcdevs.discordstatusbot.velocity.modules.metrics.VelocityMetricsModule;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.velocity.VelocityCommandHandler;

import java.io.File;

@Getter
public class Velocity extends Discord {
    public static Velocity INSTANCE;

    @NotNull private final VelocityPlugin plugin;

    public Velocity(@NotNull final VelocityPlugin plugin) {
        Velocity.INSTANCE = this;

        this.plugin = plugin;
    }

    @Override
    public @NotNull File getDirectory() {
        return this.getPlugin().getDirectory().toFile();
    }

    @Override
    public @NotNull DiscordLogger getLogger() {
        return new VelocityLogger(this.getPlugin().getLogger());
    }

    @Override
    public @NotNull DiscordPlatform getPlatform() {
        return DiscordPlatform.VELOCITY;
    }

    @Override
    public void enable() {
        super.enable();

        VelocityCommandHandler commandHandler = VelocityCommandHandler.create(this.getPlugin(), this.getPlugin().getServer());
        Metrics metrics = this.getPlugin().getMetricsFactory().make(this.getPlugin(), this.getPlatform().getId());

        this.getModuleManager().add(VelocityCommandModule.class, new VelocityCommandModule(commandHandler));
        this.getModuleManager().add(VelocityMetricsModule.class, new VelocityMetricsModule(metrics));
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

        this.getPlugin().getServer().shutdown();
    }
}
