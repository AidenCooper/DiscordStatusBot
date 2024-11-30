package net.arcdevs.discordstatusbot.common;

import lombok.Getter;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import net.arcdevs.discordstatusbot.common.exceptions.AlreadyInitializedException;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import net.arcdevs.discordstatusbot.common.managers.ModuleManager;
import net.arcdevs.discordstatusbot.common.modules.client.ClientModule;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigModule;
import net.arcdevs.discordstatusbot.common.modules.update.UpdateModule;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public abstract class Discord {
    @NotNull public abstract DiscordDependency getDependency();
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

    public Discord() {
        this.set(this);

        this.moduleManager = new ModuleManager();
    }

    public void enable() {
        // Configure backend
        this.getModuleManager().add(ConfigModule.class, new ConfigModule(this.getDirectory()));
        this.getModuleManager().add(UpdateModule.class, new UpdateModule(116918));

        this.getModuleManager().add(ClientModule.class, new ClientModule());

        // Enable backend
        this.getModuleManager().enable();
    }

    public void disable() {
        this.getModuleManager().disable();
    }

    public void reload() {
        this.getModuleManager().reload();
    }

    public void shutdown() {

    }
}
