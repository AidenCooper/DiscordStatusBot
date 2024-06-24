package net.arcdevs.discordstatusbot.common;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.exceptions.AlreadyInitializedException;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.common.managers.ModuleManager;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigModule;
import net.arcdevs.discordstatusbot.common.scheduler.DiscordScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public abstract class Discord {
    @NotNull public abstract File getDirectory();
    @NotNull public abstract DiscordLogger getLogger();
    @NotNull public abstract DiscordPlatform getPlatform();

    protected abstract void enable();
    protected abstract void disable();
    protected abstract void reload();
    protected abstract void shutdown();

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

    public final void enablePlugin() {
        // Configure backend
        this.getModuleManager().add(ConfigModule.class, new ConfigModule(Discord.get().getDirectory()));

        // Enable backend
        this.getModuleManager().enable();

        // Enable plugin
        this.enable();
    }

    public final void disablePlugin(boolean shutdown) {
        if(shutdown) {
            this.shutdown();
        } else {
            this.getScheduler().closeThreads();
            this.getModuleManager().disable();

            this.disable();
        }
    }

    public final void reloadPlugin() {
        this.getModuleManager().reload();

        this.reload();
    }
}
