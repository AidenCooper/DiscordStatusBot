package net.arcdevs.discordserverstatusupdater;

import net.arcdevs.discordserverstatusupdater.bot.DiscordBot;
import net.arcdevs.discordserverstatusupdater.bot.ServerStatus;
import net.arcdevs.discordserverstatusupdater.commands.DiscordServerCommand;
import net.arcdevs.discordserverstatusupdater.config.MainConfig;
import net.arcdevs.discordserverstatusupdater.listeners.DiscordListener;
import net.arcdevs.discordserverstatusupdater.listeners.MinecraftListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.logging.Level;

public final class MainPlugin extends JavaPlugin {
    public static MainPlugin INSTANCE = null;

    private DiscordBot discordBot;
    private MainConfig mainConfig;

    @Override
    public void onEnable() {
        MainPlugin.INSTANCE = this;
        // Configs
        try {
            this.mainConfig = new MainConfig(this);
        } catch (IOException exception) {
            exception.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // Build Discord Bot
        JDA client = this.buildClient();
        if(client == null) return;
        this.discordBot = new DiscordBot(client);

        this.getDiscordBot().setChannelID(this.getMainConfig().getDiscordChannelID());
        this.getDiscordBot().setMessageID(this.getMainConfig().getDiscordMessageID());

        // Listeners
        this.getDiscordBot().getClient().addEventListener(new DiscordListener());
        this.getServer().getPluginManager().registerEvents(new MinecraftListener(), this);

        // Commands
        Objects.requireNonNull(this.getCommand("dssu")).setExecutor(new DiscordServerCommand());
    }

    @Override
    public void onDisable() {
        this.getMainConfig().save();

        if(this.getDiscordBot() == null) return;

        this.getDiscordBot().updateStatusMessage(ServerStatus.OFFLINE);

        this.getDiscordBot().getClient().shutdown();
        try {
            if (!this.getDiscordBot().getClient().awaitShutdown(Duration.ofSeconds(5))) {
                this.getDiscordBot().getClient().shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public DiscordBot getDiscordBot() {
        return this.discordBot;
    }

    public MainConfig getMainConfig() {
        return this.mainConfig;
    }

    private JDA buildClient() {
        JDA client;

        try {
            JDALogger.setFallbackLoggerEnabled(false);
            client = JDABuilder.createLight(this.getMainConfig().getDiscordToken()).build();
        } catch (IllegalArgumentException exception) {
            MainPlugin.INSTANCE.getLogger().log(Level.SEVERE, "Insert your token into the config.yml file then restart the plugin.");
            this.getPluginLoader().disablePlugin(this);
            return null;
        } catch (InvalidTokenException exception) {
            MainPlugin.INSTANCE.getLogger().log(Level.SEVERE, "The token provided is incorrect. Update your token in the config.yml file then restart the plugin.");
            this.getPluginLoader().disablePlugin(this);
            return null;
        }

        return client;
    }
}
