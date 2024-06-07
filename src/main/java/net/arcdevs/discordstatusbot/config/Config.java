package net.arcdevs.discordstatusbot.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ParserException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class Config {
    @NotNull private final JavaPlugin plugin;
    @NotNull private final String name;

    public Config(@NotNull final JavaPlugin plugin, @NotNull final String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @NotNull
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull public abstract YamlDocument loadConfig() throws IOException, ParserException;
}
