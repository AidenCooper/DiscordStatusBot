package net.arcdevs.discordstatusbot.client;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import me.clip.placeholderapi.PlaceholderAPI;
import net.arcdevs.discordstatusbot.config.ColorSerializer;
import net.arcdevs.discordstatusbot.config.FieldSerializer;
import net.arcdevs.discordstatusbot.MainPlugin;
import net.arcdevs.discordstatusbot.dependency.DependencyType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
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
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DiscordClient {
    private static final Set<Permission> PERMISSIONS = new HashSet<Permission>(){{
        add(Permission.MESSAGE_EMBED_LINKS);
        add(Permission.MESSAGE_SEND);
        add(Permission.VIEW_CHANNEL);
    }};
    private final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);

    @Nullable private JDA client;
    @NotNull private final MainPlugin plugin;

    private MessageEmbed lastSentEmbed = null;
    private int taskID = -1;
    private boolean updatingMessage = false;

    private boolean firstLoad = true;
    private boolean reloaded = true;

    public DiscordClient(@NotNull MainPlugin plugin) {
        this.client = null;
        this.plugin = plugin;
    }

    public void load() {
        this.client = null;

        try {
            this.client = JDABuilder.createLight(this.plugin.getClientConfig().getString("token")).build();
        } catch (InvalidTokenException | IllegalArgumentException exception) {
            this.plugin.getLogger().severe("Invalid token provided in client.yml.");
            return;
        }

        if(this.plugin.getDependencyChecker().isEnabled(DependencyType.PLACEHOLDERAPI)) this.plugin.getServer().getPluginManager().registerEvents(new MinecraftListener(this), this.plugin);
        this.client.addEventListener(new DiscordListener(this));
    }

    public void unload() {
        if(this.getClient() == null) return;

        this.cancelTask();
        this.updateStatusMessage(ServerStatus.OFFLINE);

        try {
            this.getClient().shutdown();
            if (!this.getClient().awaitShutdown(Duration.ofSeconds(10))) {
                this.getClient().shutdownNow();
                this.getClient().awaitShutdown();
            }
        } catch (InterruptedException | NullPointerException | IllegalStateException ignored) {}
    }

    public void reload() {
        if(this.getClient() == null) {
            this.load();
            return;
        } else if(!this.isConnected() || (this.plugin.getDependencyChecker().isEnabled(DependencyType.PLACEHOLDERAPI) && !MinecraftListener.EXPANSIONS_LOADED)) return;

        if(this.isFirstLoad()) {
            this.setFirstLoad(false);
            this.plugin.getLogger().info("Client loaded.");
        }

        this.setReloaded(true);
        this.runTask();
    }

    @Nullable
    public JDA getClient() {
        return this.client;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isConnected() {
        return this.getClient() != null && this.getClient().getStatus() == JDA.Status.CONNECTED;
    }

    private boolean isFirstLoad() {
        return this.firstLoad;
    }

    private void setFirstLoad(final boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    private boolean isReloaded() {
        return this.reloaded;
    }

    private void setReloaded(final boolean reloaded) {
        this.reloaded = reloaded;
    }

    public void runTask() {
        this.cancelTask();

        this.taskID = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.updateStatusMessage(ServerStatus.ONLINE);
        }, 0L, 20L * this.plugin.getClientConfig().getInt("update-interval")).getTaskId();
    }

    public void updateStatusMessage(@NotNull final ServerStatus status) {
        if(!this.isConnected()) return;
        assert this.getClient() != null;

        TextChannel channel;

        try {
            channel = (TextChannel) this.getClient().getChannelCache().getElementById(ChannelType.TEXT, this.plugin.getClientConfig().getString("channel-id"));
            if(channel == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException exception) {
            if(this.isReloaded()) this.plugin.getLogger().severe("Invalid channel ID provided in client.yml.");
            this.setReloaded(false);
            return;
        }

        if(!this.checkPermissions(channel)) return;

        Route route;
        if(status == ServerStatus.ONLINE) route = Route.fromString("online");
        else {
            this.updatingMessage = false;
            this.getClient().cancelRequests();
            route = Route.fromString("offline");
        }

        YamlDocument messageConfig = this.plugin.getMessageConfig();

        String authorName = messageConfig.getString(Route.addTo(route, "author").add("name"));
        String authorURL = messageConfig.getString(Route.addTo(route, "author").add("url"));
        String authorIcon = messageConfig.getString(Route.addTo(route, "author").add("icon"));
        Color color = ColorSerializer.getColor(messageConfig, Route.addTo(route, "color"));
        String description = messageConfig.getString(Route.addTo(route, "description"));
        List<MessageEmbed.Field> fields = FieldSerializer.getFields(messageConfig, Route.addTo(route, "fields"));
        String footerText = messageConfig.getString(Route.addTo(route, "footer").add("text"));
        String footerIcon = messageConfig.getString(Route.addTo(route, "footer").add("icon"));
        boolean showTimestamp = messageConfig.getBoolean(Route.addTo(route, "footer").add("show-timestamp"));
        String titleText = messageConfig.getString(Route.addTo(route, "title").add("text"));
        String titleURL = messageConfig.getString(Route.addTo(route, "title").add("url"));
        String thumbnail = messageConfig.getString(Route.addTo(route, "thumbnail"));

        if(this.plugin.getDependencyChecker().isEnabled(DependencyType.PLACEHOLDERAPI)) {
            authorName = PlaceholderAPI.setPlaceholders(null, authorName);
            description = PlaceholderAPI.setPlaceholders(null, description);
            footerText = PlaceholderAPI.setPlaceholders(null, footerText);
            titleText = PlaceholderAPI.setPlaceholders(null, titleText);

            fields = fields.stream().map(field -> new MessageEmbed.Field(PlaceholderAPI.setPlaceholders(null, Objects.requireNonNull(field.getName())), PlaceholderAPI.setPlaceholders(null, Objects.requireNonNull(field.getValue())), field.isInline())).collect(Collectors.toList());
        }

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

        MessageEmbed embed = builder.build();
        if(this.lastSentEmbed != null && this.lastSentEmbed.equals(embed)) {
            return;
        }

        this.lastSentEmbed = embed;
        this.editMessage(channel, embed, status);
    }

    private void cancelTask() {
        if(this.taskID < 0) return;

        this.plugin.getServer().getScheduler().cancelTask(this.taskID);
        this.taskID = -1;
    }

    private boolean isValidURL(@NotNull final String url) {
        return this.URL_PATTERN.matcher(url).find();
    }

    private void editMessage(@NotNull final TextChannel channel, @NotNull final MessageEmbed embed, @NotNull final ServerStatus status) {
        if(this.updatingMessage) return;

        String messageID = this.plugin.getDataConfig().getString("message-id");

        if(StringUtils.isBlank(messageID)) {
            this.sendMessage(channel, embed, status);
        } else {
            try {
                this.updatingMessage = true;
                channel.editMessageEmbedsById(messageID, embed).queue((message -> {
                    this.updatingMessage = false;
                }), (exception) -> {
                    this.updatingMessage = false;
                    this.sendMessage(channel, embed, status);
                });
            } catch (InsufficientPermissionException exception) {
                this.plugin.getLogger().severe(String.format("Lacking \"%s\" permission.", exception.getPermission().getName()));
                this.updatingMessage = false;
            }
        }
    }

    private void sendMessage(@NotNull final TextChannel channel, @NotNull final MessageEmbed embed, @NotNull final ServerStatus status) {
        if(this.updatingMessage) return;

        if(status == ServerStatus.ONLINE) {
            try {
                this.updatingMessage = true;
                channel.sendMessageEmbeds(embed).queue((message) -> {
                    this.plugin.getDataConfig().set("message-id", message.getId());

                    try {
                        this.plugin.getDataConfig().save();
                    } catch (IOException exception) {
                        this.plugin.getLogger().severe("Could not save message-id to data.yml");
                    }

                    this.updatingMessage = false;
                }, (exception) -> {
                    this.updatingMessage = false;
                });
            } catch (InsufficientPermissionException exception) {
                this.plugin.getLogger().severe(String.format("Lacking \"%s\" permission.", exception.getPermission().getName()));
                this.updatingMessage = false;
            }
        } else {
            this.plugin.getLogger().warning("Could not find the created message to modify. Sending new message on startup.");
        }
    }

    private boolean checkPermissions(@NotNull final GuildChannel channel) {
        Set<Permission> needed = new HashSet<>();
        for(Permission permission : DiscordClient.PERMISSIONS) {
            if(!channel.getGuild().getSelfMember().hasPermission(channel, permission)) {
                needed.add(permission);
            }
        }
        if(!needed.isEmpty()) {
            if(this.isReloaded()) this.plugin.getLogger().severe(String.format("Lacking \"%s\" permission(s).", needed));
            this.setReloaded(false);
            return false;
        }

        return true;
    }
}