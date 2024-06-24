package net.arcdevs.discordstatusbot.common;

public interface DiscordLogger {
    void info(final String message);
    void warn(final String message);
    void severe(final String message);
}
