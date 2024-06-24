package net.arcdevs.discordstatusbot.common.modules.config.serializer;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class Serializer<T> {
    private final T defaultValue;

    protected Serializer(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public abstract T get(@NotNull YamlDocument document, @NotNull Route route);
    public abstract List<T> getList(@NotNull YamlDocument document, @NotNull Route route);

    public abstract void set(@NotNull YamlDocument document, @NotNull Route route, T value);
    public abstract void setList(@NotNull YamlDocument document, @NotNull Route route, List<T> values);
}
