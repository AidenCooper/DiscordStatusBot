package net.arcdevs.discordstatusbot.bungee;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class DiscordBungeePlugin extends Plugin {
    private ProxyServer server;

    private DiscordBungee bootstrap;

    @Override
    public void onEnable() {
        this.server = ProxyServer.getInstance();

        this.bootstrap = new DiscordBungee(this);
        this.bootstrap.initialize();
    }

    @Override
    public void onDisable() {
        this.bootstrap.shutdown();
    }
}
