package net.arcdevs.discordstatusbot.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordBukkitPlugin extends JavaPlugin {
    private DiscordBukkit bootstrap;

    @Override
    public void onEnable() {
        this.bootstrap = new DiscordBukkit(this);
        this.bootstrap.initialize();
    }

    @Override
    public void onDisable() {
        this.bootstrap.shutdown();
    }
}
