package net.arcdevs.discordserverstatusupdater.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.ScalarStyle;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

abstract class Config {
    @NotNull private final YamlDocument config;

    Config(@NotNull final String name, @NotNull final JavaPlugin plugin) throws IOException {
        this(YamlDocument.create(
                new File(plugin.getDataFolder(), name),
                Objects.requireNonNull(plugin.getResource(name)),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        ));
    }

    Config(@NotNull final YamlDocument config) {
        this.config = config;
    }

    @NotNull
    protected YamlDocument getConfig() {
        return this.config;
    }

    public boolean reload() {
        boolean result;

        try {
            result = this.getConfig().reload();
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        return result;
    }

    public boolean save() {
        boolean result;

        try {
            result = this.getConfig().save();
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        return result;
    }
 }
