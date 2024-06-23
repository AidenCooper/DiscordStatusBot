package net.arcdevs.discordstatusbot.bukkit.boot;

import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.bukkit.logger.BukkitLogger;
import net.arcdevs.discordstatusbot.bukkit.BukkitPlugin;
import net.arcdevs.discordstatusbot.common.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.boot.DiscordBootstrap;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

@Getter
public class BukkitBootstrap extends DiscordBootstrap<BukkitPlugin> {
    private Metrics metrics;

    public BukkitBootstrap(@NotNull final BukkitPlugin plugin) {
        super(plugin, DiscordPlatform.BUKKIT, new BukkitLogger(plugin.getLogger()), plugin.getDataFolder());
    }

    @Override
    public void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());
    }

    @Override
    public void disable() {
        if(this.getMetrics() != null) this.getMetrics().shutdown();
    }

    @Override
    public void reload() {

    }
}
