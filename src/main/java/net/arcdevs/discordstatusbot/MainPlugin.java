package net.arcdevs.discordstatusbot;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.arcdevs.discordstatusbot.client.DiscordClient;
import net.arcdevs.discordstatusbot.command.CommandHelp;
import net.arcdevs.discordstatusbot.command.MinecraftCommand;
import net.arcdevs.discordstatusbot.config.configs.ClientConfig;
import net.arcdevs.discordstatusbot.config.configs.DataConfig;
import net.arcdevs.discordstatusbot.config.configs.MessageConfig;
import net.arcdevs.discordstatusbot.dependency.DependencyChecker;
import net.arcdevs.discordstatusbot.updater.UpdateChecker;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.exception.NumberNotInRangeException;
import revxrsal.commands.help.CommandHelpWriter;

import java.io.IOException;
import java.util.logging.Level;

public final class MainPlugin extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger(MainPlugin.class);

    public static final String NAME = "DiscordStatusBot";
    public static final int RESOURCE_ID = 116918;
    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&7[&bSSB&7]&r ");

    private YamlDocument clientConfig;
    private BukkitCommandHandler commandHandler;
    private YamlDocument dataConfig;
    private DependencyChecker dependencyChecker;
    private DiscordClient discordClient;
    private YamlDocument messageConfig;
    private UpdateChecker updateChecker;

    private boolean placeholdersEnabled = false;

    @Override
    public void onEnable() {
        // Update checker
        this.updateChecker = new UpdateChecker(this, MainPlugin.RESOURCE_ID);
        this.updateChecker.checkForUpdates();

        // Dependencies
        this.dependencyChecker = new DependencyChecker(this);
        this.dependencyChecker.registerListener();

        // Configs
        try {
            this.clientConfig = new ClientConfig(this, "client.yml").loadConfig();
            this.dataConfig = new DataConfig(this, "data.yml").loadConfig();
            this.messageConfig = new MessageConfig(this, "message.yml").loadConfig();
        } catch (NullPointerException | IOException exception) {
            this.getLogger().log(Level.SEVERE, "Error loading config files.");

            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // Client
        JDALogger.setFallbackLoggerEnabled(false);
        this.discordClient = new DiscordClient(this);
        this.discordClient.load();

        // Commands
        this.commandHandler = BukkitCommandHandler.create(this);

        this.commandHandler.setHelpWriter((command, actor) -> {
            if(StringUtils.isBlank(command.getUsage())) return String.format("  &7→&r /%s &8-&r %s", command.getPath().toRealString(), command.getDescription());
            else return String.format("  &7→&r /%s &b%s&r &8-&r %s", command.getPath().toRealString(), command.getUsage(), command.getDescription());
        });
        this.commandHandler.setMessagePrefix(MainPlugin.PREFIX);

        this.commandHandler.registerContextResolver(CommandHelp.class, (context) -> {
            if (this.commandHandler.getHelpWriter() == null)
                throw new IllegalArgumentException("No help writer is registered.");
            ExecutableCommand helpCommand = context.command();
            CommandHelpWriter<?> writer = this.commandHandler.getHelpWriter();
            CommandHelp<Object> entries = new CommandHelp<>();
            CommandCategory parent = helpCommand.getParent();
            CommandPath parentPath = parent == null ? null : parent.getPath();
            this.commandHandler.getCommands().values().stream().sorted().forEach(command -> {
                if (parentPath == null || parentPath.isParentOf(command.getPath())) {
                    Object generated = writer.generate(command, context.actor());
                    if (generated != null) entries.add(generated);
                }
            });
            return entries;
        });
        this.commandHandler.registerExceptionHandler(NumberNotInRangeException.class, (actor, exception) -> {
            String parameter = exception.getParameter().getName();
            actor.reply(ChatColor.translateAlternateColorCodes('&', String.format("&c%s must be between %s and %s (found %s).", parameter.substring(0, 1).toUpperCase() + parameter.substring(1), (int) exception.getMinimum(), (int) exception.getMaximum(), exception.getInput())));


        });
        this.commandHandler.registerResponseHandler(String.class, (response, commandActor, executableCommand) -> {
            commandActor.reply(response);
        });

        this.commandHandler.register(new MinecraftCommand(this));

        if(this.commandHandler.isBrigadierSupported()) this.commandHandler.registerBrigadier();
    }

    @Override
    public void onDisable() {
        // Config
        try {
            if(this.getClientConfig() != null) {
                this.getClientConfig().reload();
                this.getClientConfig().save();
            }
            if(this.getDataConfig() != null) {
                this.getDataConfig().reload();
                this.getDataConfig().save();
            }
            if(this.getMessageConfig() != null) {
                this.getMessageConfig().reload();
                this.getMessageConfig().save();
            }
        } catch (IOException e) { this.getLogger().log(Level.WARNING, "Error saving config files."); }

        // Client
        this.getDiscordClient().unload();
    }

    public YamlDocument getClientConfig() {
        return this.clientConfig;
    }

    public BukkitCommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public YamlDocument getDataConfig() {
        return this.dataConfig;
    }

    public DependencyChecker getDependencyChecker() {
        return this.dependencyChecker;
    }

    public DiscordClient getDiscordClient() {
        return this.discordClient;
    }

    public YamlDocument getMessageConfig() {
        return this.messageConfig;
    }

    public UpdateChecker getUpdateChecker() {
        return this.updateChecker;
    }

    public boolean isPlaceholdersEnabled() {
        return this.placeholdersEnabled;
    }

    public void setPlaceholdersEnabled(final boolean placeholdersEnabled) {
        this.placeholdersEnabled = placeholdersEnabled;
    }
}
