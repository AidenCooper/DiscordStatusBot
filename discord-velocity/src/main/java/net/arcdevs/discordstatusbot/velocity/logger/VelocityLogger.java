package net.arcdevs.discordstatusbot.velocity.logger;

import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public record VelocityLogger(@NotNull Logger logger) implements DiscordLogger {
    @Override
    public void info(String message) {
        this.logger().info(message);
    }

    @Override
    public void warn(String message) {
        this.logger().warn(message);
    }

    @Override
    public void severe(String message) {
        this.logger().error(message);
    }
}
