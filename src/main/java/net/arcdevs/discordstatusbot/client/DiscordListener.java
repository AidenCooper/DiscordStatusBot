package net.arcdevs.discordstatusbot.client;

import net.arcdevs.discordstatusbot.MainPlugin;
import net.arcdevs.discordstatusbot.dependency.DependencyType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter implements Listener {
    @NotNull private final DiscordClient client;
    @NotNull private final MainPlugin plugin;

    private boolean firstTimeLoaded = true;

    public static boolean LOADED = false;

    public DiscordListener(@NotNull final DiscordClient client, @NotNull final MainPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public void onStatusChange(@NotNull final StatusChangeEvent event) {
        if(event.getNewStatus() == JDA.Status.CONNECTED) {
            if(this.plugin.getDependencyChecker().isEnabled(DependencyType.PLACEHOLDERAPI) && !DiscordListener.LOADED) return;

            if(this.firstTimeLoaded) {
                this.firstTimeLoaded = false;
                this.plugin.getLogger().info("Client loaded.");
            }

            this.client.reload();
        }
    }
 }
