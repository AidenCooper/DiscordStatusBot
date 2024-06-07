package net.arcdevs.discordstatusbot.client;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter implements Listener {
    @NotNull private final DiscordClient client;

    public DiscordListener(@NotNull final DiscordClient client) {
        this.client = client;
    }

    @Override
    public void onStatusChange(@NotNull final StatusChangeEvent event) {
        if(event.getNewStatus() == JDA.Status.CONNECTED) this.client.reload();
    }
 }
