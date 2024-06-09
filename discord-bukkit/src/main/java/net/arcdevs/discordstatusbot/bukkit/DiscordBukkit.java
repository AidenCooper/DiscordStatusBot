package net.arcdevs.discordstatusbot.bukkit;

import net.arcdevs.discordstatusbot.api.DiscordPlatform;
import net.arcdevs.discordstatusbot.common.DiscordBootstrap;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

public class DiscordBukkit extends DiscordBootstrap<DiscordBukkitPlugin> {
    public static DiscordBukkit INSTANCE;

    public DiscordBukkit(@NotNull final DiscordBukkitPlugin plugin) {
        super(plugin, DiscordPlatform.BUKKIT, plugin.getDataFolder());

        DiscordBukkit.INSTANCE = this;
    }

    private Metrics metrics;

    @Override
    public void enable() {
        this.metrics = new Metrics(this.getPlugin(), this.getPlatform().getId());

        this.getPlugin().getLogger().info("Bukkit enabled.");
    }

    @Override
    public void disable() {
        if(this.metrics != null) this.metrics.shutdown();

        this.getPlugin().getLogger().info("Bukkit disabled.");
    }
}
