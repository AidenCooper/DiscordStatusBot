package net.arcdevs.discordserverstatusupdater.bot;

import net.arcdevs.discordserverstatusupdater.MainPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class DiscordBot {
    @NotNull private final JDA client;

    @NotNull private String channelID;
    @NotNull private String messageID;

    public DiscordBot(@NotNull final JDA client) {
        this.client = client;

        this.channelID = "";
        this.messageID = "";
    }

    @NotNull
    public JDA getClient() {
        return this.client;
    }

    @NotNull
    public String getChannelID() {
        return this.channelID;
    }

    @NotNull
    public String getMessageID() {
        return this.messageID;
    }

    public void setChannelID(@NotNull final String channelID) {
        MainPlugin.INSTANCE.getMainConfig().setDiscordChannelID(channelID);
        MainPlugin.INSTANCE.getMainConfig().save();

        this.channelID = channelID;
    }

    public void setMessageID(@NotNull final String messageID) {
        MainPlugin.INSTANCE.getMainConfig().setDiscordMessageID(messageID);
        MainPlugin.INSTANCE.getMainConfig().save();

        this.messageID = messageID;
    }

    public void updateStatusMessage(@NotNull final ServerStatus status) {
        try {
            if (StringUtils.isBlank(this.getChannelID()) || this.getClient().getTextChannelById(this.getChannelID()) == null) {
                MainPlugin.INSTANCE.getLogger().log(Level.SEVERE, "Could not find the specified channel. Check the discord bot permissions and/or the channel-id in the config.yml file then use /dssu reload.");
                return;
            }
        } catch (NumberFormatException exception) {
            MainPlugin.INSTANCE.getLogger().log(Level.SEVERE, "The channel ID provided is incorrect. Update your channel ID in the config.yml file then use /dssu reload.");
            return;
        }

        if(status == ServerStatus.ONLINE) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    DiscordBot.this.sendMessage(DiscordBot.this.getOnlineStatusMessage(), ServerStatus.ONLINE);
                }
            }.runTaskLater(MainPlugin.INSTANCE, 1L);
        } else {
            this.sendMessage(this.getOfflineStatusMessage(), ServerStatus.OFFLINE);
        }
    }

    @NotNull
    private MessageEmbed getOnlineStatusMessage() {
        String ip = MainPlugin.INSTANCE.getMainConfig().getServerIP();
        String port = MainPlugin.INSTANCE.getMainConfig().getServerPort();
        String version = MainPlugin.INSTANCE.getMainConfig().getServerVersion();

        String authorName = MainPlugin.INSTANCE.getMainConfig().getOnlineAuthorName();
        String authorURL = MainPlugin.INSTANCE.getMainConfig().getOnlineAuthorURL();
        String authorIcon = MainPlugin.INSTANCE.getMainConfig().getOnlineAuthorIcon();
        String color = MainPlugin.INSTANCE.getMainConfig().getOnlineColor();
        String description = MainPlugin.INSTANCE.getMainConfig().getOnlineDescription();
        String fieldIP = MainPlugin.INSTANCE.getMainConfig().getOnlineFieldIP();
        String fieldMessage = MainPlugin.INSTANCE.getMainConfig().getOnlineFieldMessage();
        String fieldPlayerCount = MainPlugin.INSTANCE.getMainConfig().getOnlineFieldPlayerCount();
        String fieldPort = MainPlugin.INSTANCE.getMainConfig().getOnlineFieldPort();
        String fieldVersion = MainPlugin.INSTANCE.getMainConfig().getOnlineFieldVersion();
        String footerText = MainPlugin.INSTANCE.getMainConfig().getOnlineFooterText();
        String footerIcon = MainPlugin.INSTANCE.getMainConfig().getOnlineFooterIcon();
        boolean footerTimestamp = MainPlugin.INSTANCE.getMainConfig().getOnlineFooterShowTimestamp();
        String titleText = MainPlugin.INSTANCE.getMainConfig().getOnlineTitleText();
        String titleURL = MainPlugin.INSTANCE.getMainConfig().getOnlineTitleURL();
        String thumbnail = MainPlugin.INSTANCE.getMainConfig().getOnlineThumbnail();

        String message = MainPlugin.INSTANCE.getMainConfig().getOnlineMessage();
        int playerCount = MainPlugin.INSTANCE.getServer().getOnlinePlayers().size();

        Pattern pattern = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);
        boolean foundAuthorURL = pattern.matcher(authorURL).find();
        boolean foundAuthorIcon = pattern.matcher(authorIcon).find();
        boolean foundFooterIcon = pattern.matcher(footerIcon).find();
        boolean foundTitleIcon = pattern.matcher(titleURL).find();
        boolean foundThumbnail = pattern.matcher(thumbnail).find();

        EmbedBuilder builder = new EmbedBuilder();

        if(foundAuthorURL) {
            if(foundAuthorIcon) {
                builder.setAuthor(authorName, authorURL, authorIcon);
            } else {
                builder.setAuthor(authorName, authorURL);
            }
        } else {
            builder.setAuthor(authorName);
        }

        try {
            builder.setColor(Color.decode(color));
        } catch (NumberFormatException ignored) {}

        if(StringUtils.isNotBlank(description)) {
            builder.setDescription(description);
        }

        if(StringUtils.isNotBlank(footerText)) {
            if(foundFooterIcon) {
                builder.setFooter(footerText, footerIcon);
            } else {
                builder.setFooter(footerText);
            }
        }

        if(footerTimestamp) {
            builder.setTimestamp(Instant.now());
        }

        if(foundTitleIcon) {
            if(StringUtils.isBlank(titleText)) {
                builder.setTitle("Example", titleURL);
            } else {
                builder.setTitle(titleText, titleURL);
            }
        } else {
            if(StringUtils.isBlank(titleText)) {
                builder.setTitle("Example");
            } else {
                builder.setTitle(titleText);
            }
        }

        if(foundThumbnail) builder.setThumbnail(thumbnail);

        if(StringUtils.isNotBlank(port) && StringUtils.isNotBlank(fieldPort)) builder.addField(fieldPort, port, false);
        if(StringUtils.isNotBlank(version) && StringUtils.isNotBlank(fieldVersion)) builder.addField(fieldVersion, version, false);
        if(StringUtils.isNotBlank(fieldPlayerCount)) builder.addField(fieldPlayerCount, "" + playerCount, false);
        if(StringUtils.isNotBlank(message) && StringUtils.isNotBlank(fieldMessage)) builder.addField(fieldMessage, message, false);

        return builder.build();
    }

    @NotNull
    private MessageEmbed getOfflineStatusMessage() {
        String ip = MainPlugin.INSTANCE.getMainConfig().getServerIP();
        String port = MainPlugin.INSTANCE.getMainConfig().getServerPort();
        String version = MainPlugin.INSTANCE.getMainConfig().getServerVersion();

        String authorName = MainPlugin.INSTANCE.getMainConfig().getOfflineAuthorName();
        String authorURL = MainPlugin.INSTANCE.getMainConfig().getOfflineAuthorURL();
        String authorIcon = MainPlugin.INSTANCE.getMainConfig().getOfflineAuthorIcon();
        String color = MainPlugin.INSTANCE.getMainConfig().getOfflineColor();
        String description = MainPlugin.INSTANCE.getMainConfig().getOfflineDescription();
        String fieldIP = MainPlugin.INSTANCE.getMainConfig().getOfflineFieldIP();
        String fieldMessage = MainPlugin.INSTANCE.getMainConfig().getOfflineFieldMessage();
        String fieldPort = MainPlugin.INSTANCE.getMainConfig().getOfflineFieldPort();
        String fieldVersion = MainPlugin.INSTANCE.getMainConfig().getOfflineFieldVersion();
        String footerText = MainPlugin.INSTANCE.getMainConfig().getOfflineFooterText();
        String footerIcon = MainPlugin.INSTANCE.getMainConfig().getOfflineFooterIcon();
        boolean footerTimestamp = MainPlugin.INSTANCE.getMainConfig().getOfflineFooterShowTimestamp();
        String titleText = MainPlugin.INSTANCE.getMainConfig().getOfflineTitleText();
        String titleURL = MainPlugin.INSTANCE.getMainConfig().getOfflineTitleURL();
        String thumbnail = MainPlugin.INSTANCE.getMainConfig().getOfflineThumbnail();

        String message = MainPlugin.INSTANCE.getMainConfig().getOfflineMessage();

        Pattern pattern = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);
        boolean foundAuthorURL = pattern.matcher(authorURL).find();
        boolean foundAuthorIcon = pattern.matcher(authorIcon).find();
        boolean foundFooterIcon = pattern.matcher(footerIcon).find();
        boolean foundTitleIcon = pattern.matcher(titleURL).find();
        boolean foundThumbnail = pattern.matcher(thumbnail).find();

        EmbedBuilder builder = new EmbedBuilder();

        if(foundAuthorURL) {
            if(foundAuthorIcon) {
                builder.setAuthor(authorName, authorURL, authorIcon);
            } else {
                builder.setAuthor(authorName, authorURL);
            }
        } else {
            builder.setAuthor(authorName);
        }

        try {
            builder.setColor(Color.decode(color));
        } catch (NumberFormatException ignored) {}

        if(StringUtils.isNotBlank(description)) {
            builder.setDescription(description);
        }

        if(StringUtils.isNotBlank(footerText)) {
            if(foundFooterIcon) {
                builder.setFooter(footerText, footerIcon);
            } else {
                builder.setFooter(footerText);
            }
        }

        if(footerTimestamp) {
            builder.setTimestamp(Instant.now());
        }

        if(foundTitleIcon) {
            if(StringUtils.isBlank(titleText)) {
                builder.setTitle("Example", titleURL);
            } else {
                builder.setTitle(titleText, titleURL);
            }
        } else {
            if(StringUtils.isBlank(titleText)) {
                builder.setTitle("Example");
            } else {
                builder.setTitle(titleText);
            }
        }

        if(foundThumbnail) builder.setThumbnail(thumbnail);

        if(StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(fieldIP)) builder.addField(fieldIP, ip, false);
        if(StringUtils.isNotBlank(port) && StringUtils.isNotBlank(fieldPort)) builder.addField(fieldPort, port, false);
        if(StringUtils.isNotBlank(version) && StringUtils.isNotBlank(fieldVersion)) builder.addField(fieldVersion, version, false);
        if(StringUtils.isNotBlank(message) && StringUtils.isNotBlank(fieldMessage)) builder.addField(fieldMessage, message, false);

        return builder.build();
    }

    private void sendMessage(@NotNull final MessageEmbed embed, @NotNull final ServerStatus status) {
        try { Objects.requireNonNull(this.getClient().getTextChannelById(this.getChannelID())).editMessageEmbedsById(this.getMessageID(), embed).queue(null, (exception) -> this.sendNewMessage(embed, status)); }
        catch (InsufficientPermissionException exception) { MainPlugin.INSTANCE.getLogger().log(Level.SEVERE, "This bot has insufficient permissions. Change the bot's permissions then use /dssu reload."); }
        catch (IllegalArgumentException exception) { this.sendNewMessage(embed, status); }
    }

    private void sendNewMessage(@NotNull final MessageEmbed embed, @NotNull final ServerStatus status) {
        if(status == ServerStatus.ONLINE) Objects.requireNonNull(this.getClient().getTextChannelById(this.getChannelID())).sendMessageEmbeds(embed).queue((message) -> this.setMessageID(message.getId()));
        else MainPlugin.INSTANCE.getLogger().log(Level.WARNING, "Could not find the created message to modify. Sending new message on startup.");
    }
}