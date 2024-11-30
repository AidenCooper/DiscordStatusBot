package net.arcdevs.discordstatusbot.bukkit.dependency;

import me.clip.placeholderapi.PlaceholderAPI;
import net.arcdevs.discordstatusbot.bukkit.boot.Bukkit;
import net.arcdevs.discordstatusbot.common.dependency.DiscordDependency;
import org.jetbrains.annotations.NotNull;

public class BukkitDependency implements DiscordDependency {
    @Override
    public boolean isEnabled(@NotNull String name) {
        return Bukkit.INSTANCE.getPlugin().getServer().getPluginManager().isPluginEnabled(name);
    }

    @Override
    public String setPlaceholders(@NotNull String string) {
        if(this.isEnabled("PlaceholderAPI")) {
            string = PlaceholderAPI.setPlaceholders(null, string);
        }

        string = string.replace("%server_online%", Integer.toString(Bukkit.INSTANCE.getPlugin().getServer().getOnlinePlayers().size()));

        return string;
    }
}
