package net.arcdevs.discordstatusbot.api;

import org.jetbrains.annotations.NotNull;

public interface Discord {
    @NotNull DiscordPlatform getPlatform();

    void reload();

    @NotNull
    static Discord get() {
        return DiscordSupplier.get();
    }
}
