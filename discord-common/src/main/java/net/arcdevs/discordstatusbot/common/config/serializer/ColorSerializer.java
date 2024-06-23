package net.arcdevs.discordstatusbot.common.config.serializer;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColorSerializer extends Serializer<Color> {
    private ColorSerializer() {
        super(Color.decode("#23272A"));
    }

    @Override
    public Color get(@NotNull final YamlDocument document, @NotNull final Route route) {
        try { return Color.decode(document.getString(route)); }
        catch (NumberFormatException exception) { return this.getDefaultValue(); }
    }

    @Override
    public List<Color> getList(@NotNull YamlDocument document, @NotNull Route route) {
        List<Color> colors = new ArrayList<>();
        document.getList(route).forEach(colorObject -> {

        });

        return colors;
    }

    @Override
    public void set(@NotNull final YamlDocument document, @NotNull final Route route, final Color color) {
        if(color == null) document.set(route, this.getDefaultValue());
        else document.set(route, color.toString());
    }

    @Override
    public void setList(@NotNull YamlDocument document, @NotNull Route route, List<Color> colors) {
        document.set(route, colors.stream().map(Color::toString).collect(Collectors.toList()));
    }
}
