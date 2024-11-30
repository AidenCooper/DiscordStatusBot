package net.arcdevs.discordstatusbot.bungee.dependency;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.boot.Bungee;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import org.jetbrains.annotations.NotNull;

@Getter(AccessLevel.PRIVATE)
public class BungeeDependency implements DiscordDependency {
    @Override
    public boolean isEnabled(@NotNull String name) {
        return Bungee.INSTANCE.getPlugin().getProxy().getPluginManager().getPlugin(name) != null;
    }

    @Override
    public String setPlaceholders(@NotNull String string) {
        string = string.replace("%server_online%", Integer.toString(Bungee.INSTANCE.getPlugin().getProxy().getOnlineCount()));

        return string;
    }
}
