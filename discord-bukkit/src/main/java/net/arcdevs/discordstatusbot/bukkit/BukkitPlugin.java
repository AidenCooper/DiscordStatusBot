package net.arcdevs.discordstatusbot.bukkit;

import net.arcdevs.discordstatusbot.bukkit.boot.BukkitBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlugin extends JavaPlugin {
    private BukkitBootstrap bootstrap;

    @Override
    public void onEnable() {
        this.bootstrap = new BukkitBootstrap(this);
        this.bootstrap.startup();
    }

    @Override
    public void onDisable() {
        this.bootstrap.shutdown();
    }
}
