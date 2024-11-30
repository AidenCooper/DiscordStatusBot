package net.arcdevs.discordstatusbot.common.dependency;

import org.jetbrains.annotations.NotNull;

public interface DiscordDependency {
    boolean isEnabled(@NotNull String name);
    String setPlaceholders(@NotNull String string);
}
