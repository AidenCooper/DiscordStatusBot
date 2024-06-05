package net.arcdevs.discordstatusbot.client;

import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter implements Listener {
    @NotNull private final DiscordClient client;
    @NotNull private final JavaPlugin plugin;

    private boolean loaded = false;
    private boolean firstTimeLoaded = true;

    public DiscordListener(@NotNull final DiscordClient client, @NotNull final JavaPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public void onStatusChange(@NotNull final StatusChangeEvent event) {
        if(event.getNewStatus() == JDA.Status.CONNECTED && this.loaded) {
            if(this.firstTimeLoaded) {
                this.firstTimeLoaded = false;
                this.plugin.getLogger().info("Client loaded.");
            }

            this.client.reload();
        }
    }

    @EventHandler
    public void onExpansionLoaded(ExpansionsLoadedEvent event) {
        this.loaded = true;
        if(this.client.isConnected()) this.client.updateStatusMessage(ServerStatus.ONLINE);
    }
 }
