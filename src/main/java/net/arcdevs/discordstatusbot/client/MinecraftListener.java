package net.arcdevs.discordstatusbot.client;

import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class MinecraftListener implements Listener {
    @NotNull private final DiscordClient client;

    public static boolean EXPANSIONS_LOADED = false;

    public MinecraftListener(@NotNull final DiscordClient client) {
        this.client = client;
    }

    @EventHandler
    public void onExpansionLoaded(ExpansionsLoadedEvent event) {
        if(!MinecraftListener.EXPANSIONS_LOADED) MinecraftListener.EXPANSIONS_LOADED = true;
        this.client.reload();
    }
}
