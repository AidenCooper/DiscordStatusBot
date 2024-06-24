package net.arcdevs.discordstatusbot.bungee.logger;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Getter(AccessLevel.PRIVATE)
public class BungeeLogger implements DiscordLogger {
    @NotNull
    private final Logger logger;

    public BungeeLogger(@NotNull final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.getLogger().info(message);
    }

    @Override
    public void warn(String message) {
        this.getLogger().warning(message);
    }

    @Override
    public void severe(String message) {
        this.getLogger().severe(message);
    }
}
