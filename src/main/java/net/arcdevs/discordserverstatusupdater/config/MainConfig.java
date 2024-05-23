package net.arcdevs.discordserverstatusupdater.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainConfig extends Config {
    public MainConfig(@NotNull final JavaPlugin plugin) throws IOException {
        super("config.yml", plugin);
    }

    @NotNull
    public String getDiscordChannelID() {
        return this.getConfig().getString("discord.channel-id");
    }

    @NotNull
    public String getDiscordMessageID() {
        return this.getConfig().getString("discord.message-id");
    }

    @NotNull
    public String getDiscordToken() {
        return this.getConfig().getString("discord.token");
    }

    @NotNull
    public String getOfflineAuthorIcon() {
        return this.getConfig().getString("message.offline.author.icon");
    }

    @NotNull
    public String getOfflineAuthorName() {
        return this.getConfig().getString("message.offline.author.name");
    }

    @NotNull
    public String getOfflineAuthorURL() {
        return this.getConfig().getString("message.offline.author.url");
    }

    @NotNull
    public String getOfflineColor() {
        return this.getConfig().getString("message.offline.color");
    }

    @NotNull
    public String getOfflineDescription() {
        return this.getConfig().getString("message.offline.description");
    }

    @NotNull
    public String getOfflineFieldIP() {
        return this.getConfig().getString("message.offline.field.ip-address");
    }

    @NotNull
    public String getOfflineFieldMessage() {
        return this.getConfig().getString("message.offline.field.message");
    }

    @NotNull
    public String getOfflineFieldPort() {
        return this.getConfig().getString("message.offline.field.port");
    }

    @NotNull
    public String getOfflineFieldVersion() {
        return this.getConfig().getString("message.offline.field.version");
    }

    @NotNull
    public String getOfflineFooterText() {
        return this.getConfig().getString("message.offline.footer.text");
    }

    @NotNull
    public String getOfflineFooterIcon() {
        return this.getConfig().getString("message.offline.footer.icon");
    }

    public boolean getOfflineFooterShowTimestamp() {
        return this.getConfig().getBoolean("message.offline.footer.show-timestamp");
    }

    @NotNull
    public String getOfflineMessage() {
        return this.getConfig().getString("message.offline.message");
    }

    @NotNull
    public String getOfflineTitleText() {
        return this.getConfig().getString("message.offline.title.text");
    }

    @NotNull
    public String getOfflineTitleURL() {
        return this.getConfig().getString("message.offline.title.url");
    }

    @NotNull
    public String getOfflineThumbnail() {
        return this.getConfig().getString("message.offline.thumbnail");
    }

    @NotNull
    public String getOnlineAuthorIcon() {
        return this.getConfig().getString("message.online.author.icon");
    }

    @NotNull
    public String getOnlineAuthorName() {
        return this.getConfig().getString("message.online.author.name");
    }

    @NotNull
    public String getOnlineAuthorURL() {
        return this.getConfig().getString("message.online.author.url");
    }

    @NotNull
    public String getOnlineColor() {
        return this.getConfig().getString("message.online.color");
    }

    @NotNull
    public String getOnlineDescription() {
        return this.getConfig().getString("message.online.description");
    }

    @NotNull
    public String getOnlineFieldIP() {
        return this.getConfig().getString("message.online.field.ip-address");
    }

    @NotNull
    public String getOnlineFieldMessage() {
        return this.getConfig().getString("message.online.field.message");
    }

    @NotNull
    public String getOnlineFieldPlayerCount() {
        return this.getConfig().getString("message.online.field.player-count");
    }

    @NotNull
    public String getOnlineFieldPort() {
        return this.getConfig().getString("message.online.field.port");
    }

    @NotNull
    public String getOnlineFieldVersion() {
        return this.getConfig().getString("message.online.field.version");
    }

    @NotNull
    public String getOnlineFooterText() {
        return this.getConfig().getString("message.online.footer.text");
    }

    @NotNull
    public String getOnlineFooterIcon() {
        return this.getConfig().getString("message.online.footer.icon");
    }

    public boolean getOnlineFooterShowTimestamp() {
        return this.getConfig().getBoolean("message.online.footer.show-timestamp");
    }

    @NotNull
    public String getOnlineMessage() {
        return this.getConfig().getString("message.online.message");
    }

    @NotNull
    public String getOnlineTitleText() {
        return this.getConfig().getString("message.online.title.text");
    }

    @NotNull
    public String getOnlineTitleURL() {
        return this.getConfig().getString("message.online.title.url");
    }

    @NotNull
    public String getOnlineThumbnail() {
        return this.getConfig().getString("message.online.thumbnail");
    }

    @NotNull
    public String getServerIP() {
        return this.getConfig().getString("message.ip-address");
    }

    @NotNull
    public String getServerPort() {
        return this.getConfig().getString("message.port");
    }

    @NotNull
    public String getServerVersion() {
        return this.getConfig().getString("message.version");
    }

    public void setDiscordChannelID(@NotNull final String discordChannelID) {
        this.getConfig().set("discord.channel-id", discordChannelID);
    }

    public void setDiscordMessageID(@NotNull final String discordMessageID) {
        this.getConfig().set("discord.message-id", discordMessageID);
    }

    public void setServerIPAddress(@NotNull final String serverIPAddress) {
        this.getConfig().set("server.ip-address", serverIPAddress);
    }

    public void setServerName(@NotNull final String serverName) {
        this.getConfig().set("server.name", serverName);
    }
}
