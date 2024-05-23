package net.arcdevs.discordserverstatusupdater.listeners;

import net.arcdevs.discordserverstatusupdater.MainPlugin;
import net.arcdevs.discordserverstatusupdater.bot.ServerStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class MinecraftListener implements Listener {
    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        MainPlugin.INSTANCE.getDiscordBot().updateStatusMessage(ServerStatus.ONLINE);
    }

    @EventHandler
    public void onPlayerQuit(@NotNull final PlayerQuitEvent event) {
        MainPlugin.INSTANCE.getDiscordBot().updateStatusMessage(ServerStatus.ONLINE);
    }
}
