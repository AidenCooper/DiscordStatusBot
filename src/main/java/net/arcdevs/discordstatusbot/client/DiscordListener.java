package net.arcdevs.discordstatusbot.client;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    @NotNull private final DiscordClient client;
    @NotNull private final JavaPlugin plugin;

    public DiscordListener(@NotNull final DiscordClient client, @NotNull final JavaPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public void onStatusChange(@NotNull final StatusChangeEvent event) {
        if(event.getNewStatus() == JDA.Status.CONNECTED) {
            this.plugin.getLogger().info("Client loaded.");
            this.client.reload();
        }
    }
 }
