package net.arcdevs.discordstatusbot.common;

import net.arcdevs.discordstatusbot.common.config.ConfigManager;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface Discord {
    @NotNull DiscordPlatform getPlatform();
    @NotNull DiscordLogger getLogger();
    @NotNull File getDirectory();

    @NotNull ConfigManager getConfigManager();

    @NotNull
    static Discord get() {
        return DiscordSupplier.get();
    }
}
