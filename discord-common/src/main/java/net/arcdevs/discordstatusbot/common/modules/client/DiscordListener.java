package net.arcdevs.discordstatusbot.common.modules.client;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigModule;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigName;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onStatusChange(@NotNull StatusChangeEvent event) {
        if (event.getNewStatus() == JDA.Status.CONNECTED) {
            JDAHandler client = ((ClientModule) Discord.get().getModuleManager().get(ClientModule.class)).getClient();
            if (client == null) return;

            client.scheduleUpdate(true);
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        YamlDocument dataConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.DATA);
        if(dataConfig == null) return;

        if(event.getMessageId().equals(dataConfig.getString("message-id"))) {
            ((ClientModule) Discord.get().getModuleManager().get(ClientModule.class)).getClient().setLastSentEmbed(null);
        }
    }
}