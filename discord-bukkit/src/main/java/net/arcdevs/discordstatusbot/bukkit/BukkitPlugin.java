package net.arcdevs.discordstatusbot.bukkit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.bukkit.boot.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter(AccessLevel.PRIVATE)
public final class BukkitPlugin extends JavaPlugin {
    private Bukkit bukkit;

    @Override
    public void onEnable() {
        this.setBukkit(new Bukkit(this));
        this.getBukkit().enable();
    }

    @Override
    public void onDisable() {
        this.getBukkit().disable();
    }
}
