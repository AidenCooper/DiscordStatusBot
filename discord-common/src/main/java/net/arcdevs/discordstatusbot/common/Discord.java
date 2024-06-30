package net.arcdevs.discordstatusbot.common;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.exceptions.AlreadyInitializedException;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.common.managers.ModuleManager;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigModule;
import net.arcdevs.discordstatusbot.common.modules.update.UpdateModule;
import net.arcdevs.discordstatusbot.common.scheduler.DiscordScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public abstract class Discord {
    @NotNull public abstract File getDirectory();
    @NotNull public abstract DiscordLogger getLogger();
    @NotNull public abstract DiscordPlatform getPlatform();

    private static Discord discord;

    @NotNull
    public static Discord get() {
        return Discord.discord;
    }

    public void set(@NotNull final Discord discord) {
        if (Discord.discord != null) throw new AlreadyInitializedException();
        Discord.discord = discord;
    }

    private final ModuleManager moduleManager;
    private final DiscordScheduler scheduler;

    public Discord() {
        this.set(this);

        this.moduleManager = new ModuleManager();
        this.scheduler = new DiscordScheduler();
    }

    public void enable() {
        // Configure backend
        this.getModuleManager().add(ConfigModule.class, new ConfigModule(this.getDirectory()));
        this.getModuleManager().add(UpdateModule.class, new UpdateModule(116918));

        // Enable backend
        this.getModuleManager().enable();
    }

    public void disable() {
        this.getScheduler().closeThreads();
        this.getModuleManager().disable();
    }

    public void reload() {
        this.getModuleManager().reload();
    }

    public void shutdown() {

    }
}
