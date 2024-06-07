package net.arcdevs.discordstatusbot.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorSerializer {
    private static final Color DEFAULT_COLOR = Color.decode("#23272A");

    public static Color getColor(@NotNull final YamlDocument document, @NotNull final Route route) {
        try { return Color.decode(document.getString(route)); }
        catch (NumberFormatException exception) { return ColorSerializer.DEFAULT_COLOR; }
    }

    public static void setColor(@NotNull final YamlDocument document, @NotNull final Route route, final Color color) {
        if(color == null) document.set(route, ColorSerializer.DEFAULT_COLOR.toString());
        else document.set(route, color.toString());
    }
}
