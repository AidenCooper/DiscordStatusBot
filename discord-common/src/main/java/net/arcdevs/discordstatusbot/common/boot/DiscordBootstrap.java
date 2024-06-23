package net.arcdevs.discordstatusbot.common.boot;

import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ParserException;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.DiscordSupplier;
import net.arcdevs.discordstatusbot.common.config.ConfigManager;
import net.arcdevs.discordstatusbot.common.logger.DiscordLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class DiscordBootstrap<T> implements Discord {
    public abstract void enable();
    public abstract void disable();
    public abstract void reload();

    @NotNull private final T plugin;
    @NotNull private final DiscordPlatform platform;
    @NotNull private final DiscordLogger logger;
    @NotNull private final File directory;

    private ConfigManager configManager;

    public DiscordBootstrap(@NotNull final T plugin, @NotNull final DiscordPlatform platform, @NotNull final DiscordLogger logger, @NotNull final File directory) {
        this.plugin = plugin;
        this.platform = platform;
        this.logger = logger;
        this.directory = directory;

        DiscordSupplier.set(this);
    }

    public void startup() {
        this.reload();

        try {
            this.configManager = new ConfigManager(this.getDirectory());
        } catch (NullPointerException | IOException | ParserException exception) {
            this.getLogger().severe(exception.getMessage());
            this.shutdown();
            return;
        }

        this.enable();
    }

    public void shutdown() {
        this.disable();
    }
}
