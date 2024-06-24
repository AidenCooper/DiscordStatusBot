package net.arcdevs.discordstatusbot.velocity.logger;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Getter(AccessLevel.PRIVATE)
public class VelocityLogger implements DiscordLogger {
    @NotNull private final Logger logger;

    public VelocityLogger(@NotNull final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.getLogger().info(message);
    }

    @Override
    public void warn(String message) {
        this.getLogger().warn(message);
    }

    @Override
    public void severe(String message) {
        this.getLogger().error(message);
    }
}
