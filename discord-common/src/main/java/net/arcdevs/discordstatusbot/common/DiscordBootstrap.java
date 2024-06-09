package net.arcdevs.discordstatusbot.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.arcdevs.discordstatusbot.api.Discord;
import net.arcdevs.discordstatusbot.api.DiscordPlatform;
import net.arcdevs.discordstatusbot.api.DiscordSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
@RequiredArgsConstructor
public abstract class DiscordBootstrap<T> implements Discord {
    @NotNull private T plugin;
    @NotNull private DiscordPlatform platform;

    public DiscordBootstrap(@NotNull final T plugin, @NotNull final DiscordPlatform platform, @NotNull final File directory) {
        DiscordSupplier.set(this);

        this.plugin = plugin;
        this.platform = platform;
    }

    public final void initialize() {
        reload();

        try {
            enable();
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
            return;
        }

        // Updater
    }

    public final void shutdown() {
        disable();
    }

    public final void reload() {

    }

    public abstract void enable();
    public abstract void disable();
}
