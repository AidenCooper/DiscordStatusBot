package net.arcdevs.discordstatusbot.common.modules.client;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigModule;
import net.arcdevs.discordstatusbot.common.modules.config.ConfigName;
import net.arcdevs.discordstatusbot.common.modules.config.serializer.ColorSerializer;
import net.arcdevs.discordstatusbot.common.modules.config.serializer.FieldSerializer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JDAHandler {
    private static final Set<Permission> PERMISSIONS = new HashSet<Permission>(){{
        add(Permission.MESSAGE_EMBED_LINKS);
        add(Permission.MESSAGE_SEND);
        add(Permission.VIEW_CHANNEL);
    }};
    private final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);

    @Getter private JDA jda = null;

    @Getter @Setter private ScheduledExecutorService service = null;
    @Getter @Setter private MessageEmbed lastSentEmbed = null;
    @Getter @Setter private boolean frozenChannel = false;
    @Getter @Setter private boolean frozenPermission = false;
    @Getter @Setter private boolean updatingMessage = false;

    public void enable() {
        if(this.isEnabled()) return;

        YamlDocument clientConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.CLIENT);
        if(clientConfig == null) {
            Discord.get().getLogger().severe("Error retrieving config files.");
            return;
        }

        try {
            this.jda = JDABuilder.createLight(clientConfig.getString("token")).build();
        } catch (InvalidTokenException | IllegalArgumentException exception) {
            Discord.get().getLogger().severe("Invalid token provided in client.yml.");
            return;
        }

        this.getJda().addEventListener(new DiscordListener());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void disable() {
        if(!this.isEnabled()) return;

        this.getJda().cancelRequests();
        this.getService().shutdownNow();
        this.update(false);

        try {
            this.getJda().awaitShutdown(Duration.ofSeconds(1));
            this.getJda().shutdown();
            if (!this.getJda().awaitShutdown(Duration.ofSeconds(10))) {
                this.getJda().shutdownNow();
                this.getJda().awaitShutdown();
            }
        } catch (Exception ignored) {}
    }

    public void reload() {
        this.setLastSentEmbed(null);

        if(!this.isEnabled()) this.enable();
        else {
            YamlDocument clientConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.CLIENT);
            if(clientConfig == null) return;

            this.scheduleUpdate(false);
        }
    }

    public boolean isEnabled() {
        return this.jda != null && this.jda.getStatus() == JDA.Status.CONNECTED;
    }

    public boolean isFrozen() {
        if(!this.isEnabled()) return false;

        YamlDocument clientConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.CLIENT);
        if(clientConfig == null) return false;

        TextChannel channel;
        try {
            channel = this.getJda().getTextChannelCache().getElementById(clientConfig.getString("channel-id"));
            this.setFrozenChannel(false);
        } catch (Exception exception) {
            if(!this.isFrozenChannel()) Discord.get().getLogger().severe("Could not find the channel-id provided in client.yml.");
            this.setFrozenChannel(true);
            return true;
        }
        if(channel == null) {
            if(!this.isFrozenChannel()) Discord.get().getLogger().severe("Could not find the channel-id provided in client.yml.");
            this.setFrozenChannel(true);
            return true;
        }

        Member member = channel.getGuild().getSelfMember();
        Set<Permission> needed = new HashSet<>();
        for(Permission permission : JDAHandler.PERMISSIONS) {
            if(!member.hasPermission(channel, permission)) {
                needed.add(permission);
            }
        }

        if(!needed.isEmpty()) {
            if(!this.isFrozenPermission()) Discord.get().getLogger().severe(String.format("Lacking \"%s\" permission(s).", needed));
            this.setFrozenPermission(true);
            return true;
        }

        this.setFrozenChannel(false);
        this.setFrozenPermission(false);

        return false;
    }

    public void scheduleUpdate(boolean delay) {
        if(!this.isEnabled()) return;

        if(this.getService() == null || this.getService().isTerminated() || this.getService().isShutdown()) {
            if(this.getService() == null) this.setService(Executors.newSingleThreadScheduledExecutor());

            YamlDocument clientConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.CLIENT);
            if(clientConfig == null) {
                Discord.get().getLogger().severe("Client disconnected. Error retrieving config files.");
                return;
            }

            if(delay) this.getService().scheduleAtFixedRate(() -> this.update(true), clientConfig.getLong("delay-interval"), clientConfig.getLong("update-interval"), TimeUnit.SECONDS);
            else this.getService().scheduleAtFixedRate(() -> this.update(true), 0L, clientConfig.getLong("update-interval"), TimeUnit.SECONDS);
        }
    }

    private MessageEmbed getMessage(boolean enabled) {
        YamlDocument messageConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.MESSAGE);
        if(messageConfig == null) return null;

        Route route;
        if(enabled) route = Route.fromString("online");
        else route = Route.fromString("offline");

        String authorName = messageConfig.getString(Route.addTo(route, "author").add("name"));
        String authorURL = messageConfig.getString(Route.addTo(route, "author").add("url"));
        String authorIcon = messageConfig.getString(Route.addTo(route, "author").add("icon"));
        Color color = new ColorSerializer().get(messageConfig, Route.addTo(route, "color"));
        String description = messageConfig.getString(Route.addTo(route, "description"));
        List<MessageEmbed.Field> fields = new FieldSerializer().getList(messageConfig, Route.addTo(route, "fields"));
        String footerText = messageConfig.getString(Route.addTo(route, "footer").add("text"));
        String footerIcon = messageConfig.getString(Route.addTo(route, "footer").add("icon"));
        boolean showTimestamp = messageConfig.getBoolean(Route.addTo(route, "footer").add("show-timestamp"));
        String titleText = messageConfig.getString(Route.addTo(route, "title").add("text"));
        String titleURL = messageConfig.getString(Route.addTo(route, "title").add("url"));
        String thumbnail = messageConfig.getString(Route.addTo(route, "thumbnail"));

        authorName = Discord.get().getDependency().setPlaceholders(authorName);
        description = Discord.get().getDependency().setPlaceholders(description);
        footerText = Discord.get().getDependency().setPlaceholders(footerText);
        titleText = Discord.get().getDependency().setPlaceholders(titleText);

        fields = fields.stream().map(field -> new MessageEmbed.Field(
                Discord.get().getDependency().setPlaceholders(Objects.requireNonNull(field.getName())),
                Discord.get().getDependency().setPlaceholders(Objects.requireNonNull(field.getValue())),
                field.isInline())
        ).collect(Collectors.toList());

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setDescription(description);

        if(!this.isValidURL(authorURL)) builder.setAuthor(authorName);
        else if(!this.isValidURL(authorIcon)) builder.setAuthor(authorName, authorURL);
        else builder.setAuthor(authorName, authorURL, authorIcon);

        if(!this.isValidURL(footerIcon)) builder.setFooter(footerText);
        else builder.setFooter(footerText, footerIcon);

        if(!this.isValidURL(titleURL)) builder.setTitle(titleText);
        else builder.setTitle(titleText, titleURL);

        if(this.isValidURL(thumbnail)) builder.setThumbnail(thumbnail);

        if(showTimestamp) builder.setTimestamp(Instant.now().truncatedTo(ChronoUnit.MINUTES));

        for(MessageEmbed.Field field : fields) builder.addField(field);

        return builder.build();
    }

    private void update(boolean enabled) {
        if(!this.isEnabled() || this.isFrozen()) return;

        YamlDocument clientConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.CLIENT);
        YamlDocument dataConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.DATA);
        if(clientConfig == null || dataConfig == null) return;

        TextChannel channel = this.getJda().getTextChannelCache().getElementById(clientConfig.getString("channel-id"));
        assert channel != null;

        MessageEmbed embed = this.getMessage(enabled);

        if((this.getLastSentEmbed() != null && this.getLastSentEmbed().equals(embed)) || embed == null) return;

        this.setLastSentEmbed(embed);
        this.editMessage(channel, dataConfig.getString("message-id"), embed, enabled);
    }

    private void editMessage(@NotNull final TextChannel channel, @Nullable final String messageID, @NotNull final MessageEmbed embed, final boolean enabled) {
        if(this.isUpdatingMessage()) return;

        if(messageID == null || StringUtils.isBlank(messageID)) {
            this.sendMessage(channel, embed, enabled);
        } else {
            try {
                this.setUpdatingMessage(true);

                channel.editMessageEmbedsById(messageID, embed).queue((message -> {
                    this.setUpdatingMessage(false);
                }), (exception) -> {
                    this.setUpdatingMessage(false);
                    this.sendMessage(channel, embed, enabled);
                });
            } catch (InsufficientPermissionException exception) {
                Discord.get().getLogger().severe(String.format("Lacking \"%s\" permission.", exception.getPermission().getName()));
                this.setFrozenPermission(true);
                this.setUpdatingMessage(false);
            }
        }
    }

    private void sendMessage(@NotNull final TextChannel channel, @NotNull final MessageEmbed embed, final boolean enabled) {
        if(this.isUpdatingMessage()) return;

        if(enabled) {
            try {
                this.setUpdatingMessage(true);

                YamlDocument dataConfig = ((ConfigModule) Discord.get().getModuleManager().get(ConfigModule.class)).getConfig(ConfigName.DATA);
                if(dataConfig == null) {
                    this.setUpdatingMessage(false);
                    Discord.get().getLogger().severe("Error retrieving config files.");
                    return;
                }

                channel.sendMessageEmbeds(embed).queue((message) -> {
                    dataConfig.set("message-id", message.getId());

                    try { dataConfig.save(); }
                    catch (IOException exception) { Discord.get().getLogger().severe("Could not save message-id to data.yml"); }

                    this.setUpdatingMessage(false);
                }, (exception) -> {
                    this.setUpdatingMessage(false);
                    this.setLastSentEmbed(null);
                });
            } catch (InsufficientPermissionException exception) {
                Discord.get().getLogger().severe(String.format("Lacking \"%s\" permission.", exception.getPermission().getName()));
                this.setFrozenPermission(true);
                this.setUpdatingMessage(false);
            }
        } else {
            Discord.get().getLogger().warn("Could not find the created message to modify. Sending new message on startup.");
        }
    }

    private boolean isValidURL(@NotNull final String url) {
        return this.URL_PATTERN.matcher(url).find();
    }
}
