package net.arcdevs.discordstatusbot.common.modules;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public abstract class DiscordModule {
    protected abstract void enable();
    protected abstract void disable();
    protected abstract void reload();

    private boolean enabled;

    public DiscordModule() {
        this.setEnabled(false);
    }

    public final void enableModule() {
        if(!this.isEnabled()) this.setEnabled(true);
        else return;

        this.enable();
    }

    public final void disableModule() {
        if(this.isEnabled()) this.setEnabled(false);
        else return;

        this.disable();
    }

    public final void reloadModule() {
        if(this.isEnabled()) this.reload();
    }
}
