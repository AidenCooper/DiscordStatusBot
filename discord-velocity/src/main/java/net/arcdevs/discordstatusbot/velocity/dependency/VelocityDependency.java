package net.arcdevs.discordstatusbot.velocity.dependency;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import net.arcdevs.discordstatusbot.velocity.boot.Velocity;
import org.jetbrains.annotations.NotNull;

@Getter(AccessLevel.PRIVATE)
public class VelocityDependency implements DiscordDependency {
    @Override
    public boolean isEnabled(@NotNull String name) {
        return Velocity.INSTANCE.getPlugin().getServer().getPluginManager().isLoaded(name.toLowerCase());
    }

    @Override
    public String setPlaceholders(@NotNull String string) {
        string = string.replace("%server_online%", Integer.toString(Velocity.INSTANCE.getPlugin().getServer().getPlayerCount()));

        return string;
    }
}
