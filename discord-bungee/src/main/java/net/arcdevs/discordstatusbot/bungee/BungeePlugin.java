package net.arcdevs.discordstatusbot.bungee;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.boot.BungeeBootstrap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class BungeePlugin extends Plugin {
    @Getter private ProxyServer server;

    private BungeeBootstrap bootstrap;

    @Override
    public void onEnable() {
        this.bootstrap = new BungeeBootstrap(this);
        this.bootstrap.startup();
    }

    @Override
    public void onDisable() {
        this.bootstrap.shutdown();
    }
}
