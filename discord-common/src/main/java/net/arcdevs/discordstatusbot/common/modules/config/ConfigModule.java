package net.arcdevs.discordstatusbot.common.modules.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.ScalarStyle;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ParserException;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.exceptions.ModuleException;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;
import net.arcdevs.discordstatusbot.common.util.HandledLinkedHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter(AccessLevel.PRIVATE)
public class ConfigModule extends DiscordModule {
    @NotNull private final HandledLinkedHashMap<ConfigName, YamlDocument> configs;
    @NotNull private final File directory;

    public ConfigModule(@NotNull final File directory) {
        this.configs = new HandledLinkedHashMap<>();
        this.directory = directory;
    }

    @Nullable
    public YamlDocument getConfig(@NotNull final ConfigName name) {
        return this.getConfigs().get(name);
    }

    @Nullable
    public ConfigName getName(@NotNull final YamlDocument config) {
        return this.getConfigs().getKey(config);
    }

    public void reloadConfig(@NotNull final ConfigName name) {
        try { Objects.requireNonNull(this.getConfig(name)).reload(); }
        catch (IOException | NullPointerException exception) { throw new ModuleException("Error reloading config \"%s\".", false); }
    }

    public void saveConfig(@NotNull final ConfigName name) {
        try { Objects.requireNonNull(this.getConfig(name)).save(); }
        catch (IOException | NullPointerException exception) { throw new ModuleException("Error saving config \"%s\".", false); }
    }

    @Override
    protected void enable() {
        for(ConfigName name : ConfigName.values()) {
            this.getConfigs().put(name, () -> {
                YamlDocument config;
                try {
                    config = YamlDocument.create(
                            new File(this.getDirectory(), name.getName()),
                            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(name.getName())),
                            GeneralSettings.DEFAULT,
                            LoaderSettings.builder().setAutoUpdate(true).build(),
                            DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
                            UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
                        ).build()
                    );
                } catch (IOException | NullPointerException | ParserException exception) {
                    throw new ModuleException(String.format("Error loading config file \"%s\".", name.getName()), exception.getCause(), true);
                }

                return config;
            });
        }
    }

    @Override
    protected void disable() {
        for(ConfigName name : this.getConfigs().keySet()) this.saveConfig(name);
    }

    @Override
    protected void reload() {
        for(ConfigName name : this.getConfigs().keySet()) this.reloadConfig(name);
    }
}
