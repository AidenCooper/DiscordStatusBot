package net.arcdevs.discordstatusbot.common.modules.client;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;

@Getter(AccessLevel.PUBLIC)
public class ClientModule extends DiscordModule {

    private final JDAHandler client;

    public ClientModule() {
        this.client = new JDAHandler();
    }

    @Override
    protected void enable() {
        this.getClient().enable();
    }

    @Override
    protected void disable() {
        this.getClient().disable();
    }

    @Override
    protected void reload() {
        this.getClient().reload();
    }
}
