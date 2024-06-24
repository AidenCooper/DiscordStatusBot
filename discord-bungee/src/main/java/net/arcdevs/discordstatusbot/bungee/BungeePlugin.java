package net.arcdevs.discordstatusbot.bungee;

import lombok.Getter;
import net.arcdevs.discordstatusbot.bungee.boot.Bungee;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class BungeePlugin extends Plugin {
    private Bungee bungee;

    @Override
    public void onEnable() {
        this.bungee = new Bungee(this);
        this.bungee.enablePlugin();
    }

    @Override
    public void onDisable() {
        this.bungee.disablePlugin();
    }
}
