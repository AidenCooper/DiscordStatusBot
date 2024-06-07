package net.arcdevs.discordstatusbot.config.configs;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.ScalarStyle;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ParserException;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.arcdevs.discordstatusbot.config.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ClientConfig extends Config {
    public ClientConfig(@NotNull final JavaPlugin plugin, @NotNull final String name) {
        super(plugin, name);
    }

    @NotNull
    @Override
    public YamlDocument loadConfig() throws IOException, ParserException {
        return YamlDocument.create(
            new File(this.getPlugin().getDataFolder(), this.getName()),
            Objects.requireNonNull(this.getPlugin().getResource(this.getName())),
            GeneralSettings.DEFAULT,
            LoaderSettings.builder().setAutoUpdate(true).build(),
            DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
            UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
        ).build());
    }
}
