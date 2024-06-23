package net.arcdevs.discordstatusbot.common.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ParserException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Getter
public abstract class Config {
    @NotNull private final String name;

    protected Config(@NotNull final String name) {
        this.name = name;
    }

    @NotNull public abstract YamlDocument loadConfig() throws IOException, ParserException;
}
