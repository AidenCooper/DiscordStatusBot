package net.arcdevs.discordstatusbot.bukkit;

import net.arcdevs.discordstatusbot.bukkit.boot.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlugin extends JavaPlugin {
    private Bukkit bukkit;

    @Override
    public void onEnable() {
        this.bukkit = new Bukkit(this);
        this.bukkit.enablePlugin();
    }

    @Override
    public void onDisable() {
        this.bukkit.disablePlugin();
    }
}
