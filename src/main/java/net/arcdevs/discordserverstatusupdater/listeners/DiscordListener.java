package net.arcdevs.discordserverstatusupdater.listeners;

import net.arcdevs.discordserverstatusupdater.MainPlugin;
import net.arcdevs.discordserverstatusupdater.bot.ServerStatus;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        MainPlugin.INSTANCE.getDiscordBot().updateStatusMessage(ServerStatus.ONLINE);
    }
}
