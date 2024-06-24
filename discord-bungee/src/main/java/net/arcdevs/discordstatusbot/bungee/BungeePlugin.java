package net.arcdevs.discordstatusbot.bungee;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.bungee.boot.Bungee;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
@Setter(AccessLevel.PRIVATE)
public final class BungeePlugin extends Plugin {
    private Bungee bungee;

    @Override
    public void onEnable() {
        this.setBungee(new Bungee(this));
        this.getBungee().enablePlugin();


    }

    @Override
    public void onDisable() {
        this.getBungee().disablePlugin(false);
    }
}
