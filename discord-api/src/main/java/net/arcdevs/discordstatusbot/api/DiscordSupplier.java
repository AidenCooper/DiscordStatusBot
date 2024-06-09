package net.arcdevs.discordstatusbot.api;

import lombok.experimental.UtilityClass;
import net.arcdevs.discordstatusbot.api.exceptions.AlreadyInitializedException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@UtilityClass
public class DiscordSupplier {
    private Discord discord;

    @SuppressWarnings("ConstantConditions")
    public void set(@NotNull final Discord discord) {
        if(DiscordSupplier.discord != null) throw new AlreadyInitializedException();
        DiscordSupplier.discord = Objects.requireNonNull(discord);
    }

    @NotNull
    public Discord get() {
        return DiscordSupplier.discord;
    }
}
