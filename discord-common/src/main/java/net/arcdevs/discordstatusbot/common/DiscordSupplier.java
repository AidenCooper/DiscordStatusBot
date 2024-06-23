package net.arcdevs.discordstatusbot.common;

import lombok.experimental.UtilityClass;
import net.arcdevs.discordstatusbot.common.exceptions.AlreadyInitializedException;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DiscordSupplier {
    private Discord discord;

    @NotNull
    public Discord get() {
        return DiscordSupplier.discord;
    }

    public void set(@NotNull final Discord discord) {
        if(DiscordSupplier.discord != null) throw new AlreadyInitializedException();
        DiscordSupplier.discord = discord;
    }
}
