package net.arcdevs.discordstatusbot.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscordPlatform {
    BUKKIT("Bukkit", 22159),
    BUNGEE("Bungee", 22185),
    VELOCITY("Velocity", 22186);

    private final String name;
    private final int metricsID;
}
