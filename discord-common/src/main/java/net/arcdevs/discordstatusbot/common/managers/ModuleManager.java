package net.arcdevs.discordstatusbot.common.managers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;
import net.arcdevs.discordstatusbot.common.util.HandledLinkedHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class ModuleManager {
    private final HandledLinkedHashMap<Class<? extends DiscordModule>, DiscordModule> modules;

    private boolean enabled;

    public ModuleManager() {
        this.modules = new HandledLinkedHashMap<>();

        this.setEnabled(false);
    }

    public void add(@NotNull final Class<? extends DiscordModule> clazz, @NotNull final DiscordModule module) {
        this.add(clazz, () -> module);
    }

    public void add(@NotNull final Class<? extends DiscordModule> clazz, @NotNull final Supplier<DiscordModule> supplier) {
        this.getModules().put(clazz, supplier);

        if(this.isEnabled()) this.getModules().get(clazz).enableModule();
    }

    public void remove(@NotNull final Class<? extends DiscordModule> clazz) {
        DiscordModule module = this.getModules().remove(clazz);
        if(module != null) module.disableModule();
    }

    public void enable() {
        if(!this.isEnabled()) this.setEnabled(true);
        else return;

        for (DiscordModule module : this.getModules().values()) {
            if (!module.isEnabled()) module.enableModule();
        }
    }

    public void disable() {
        if(this.isEnabled()) this.setEnabled(false);
        else return;

        for(DiscordModule module : this.getModules().values()) {
            if(module.isEnabled()) module.disableModule();
        }
    }

    public void reload() {
        if(!this.isEnabled()) return;

        for(DiscordModule module : this.getModules().values()) {
            if(module.isEnabled()) module.reloadModule();
        }
    }
}
