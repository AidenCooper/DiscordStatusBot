package net.arcdevs.discordstatusbot.common.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.ScalarStyle;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigManager {
    @NotNull private final String CLIENT_NAME = "client.yml";
    @NotNull private final String DATA_NAME = "data.yml";
    @NotNull private final String MESSAGE_NAME = "message.yml";

    @Getter private final YamlDocument clientConfig, dataConfig, messageConfig;

    public ConfigManager(@NotNull final File directory) throws IOException {
        this.clientConfig = YamlDocument.create(
            new File(directory, this.CLIENT_NAME),
            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(this.CLIENT_NAME)),
            GeneralSettings.DEFAULT,
            LoaderSettings.builder().setAutoUpdate(true).build(),
            DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
            UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
        ).build());
        this.dataConfig = YamlDocument.create(
            new File(directory, this.DATA_NAME),
            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(this.DATA_NAME)),
            GeneralSettings.DEFAULT,
            LoaderSettings.builder().setAutoUpdate(true).build(),
            DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
            UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
        ).build());
        this.messageConfig = YamlDocument.create(
            new File(directory, this.MESSAGE_NAME),
            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(this.MESSAGE_NAME)),
            GeneralSettings.DEFAULT,
            LoaderSettings.builder().setAutoUpdate(true).build(),
            DumperSettings.builder().setStringStyle(ScalarStyle.SINGLE_QUOTED).build(),
            UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
        ).build());
    }
}
