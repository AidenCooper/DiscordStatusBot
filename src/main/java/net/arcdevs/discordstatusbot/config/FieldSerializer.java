package net.arcdevs.discordstatusbot.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FieldSerializer {
    private static final MessageEmbed.Field DEFAULT_FIELD = new MessageEmbed.Field("", "", false);

    public static MessageEmbed.Field getField(@NotNull final YamlDocument document, @NotNull final Route route) {
        String name = document.getString(route + ".name");
        String value = document.getString(route + ".value");
        boolean inline = document.getBoolean(route + ".inline");

        if(name == null) name = FieldSerializer.DEFAULT_FIELD.getName();
        if(value == null) value = FieldSerializer.DEFAULT_FIELD.getValue();

        return new MessageEmbed.Field(name, value, inline);
    }

    public static List<MessageEmbed.Field> getFields(@NotNull final YamlDocument document, @NotNull final Route route) {
        List<MessageEmbed.Field> fields = new ArrayList<>();

        document.getList(route).forEach(fieldObject -> {
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) fieldObject;

            if(map == null) {
                fields.add(FieldSerializer.DEFAULT_FIELD);
                return;
            }

            String name = (String) map.get("name");
            String value = (String) map.get("value");
            Boolean inline = (Boolean) map.get("inline");

            if(name == null) name = FieldSerializer.DEFAULT_FIELD.getName();
            if(value == null) value = FieldSerializer.DEFAULT_FIELD.getValue();
            if(inline == null) inline = FieldSerializer.DEFAULT_FIELD.isInline();

            fields.add(new MessageEmbed.Field(name, value, inline));
        });

        return fields;
    }

    public static void setField(@NotNull final YamlDocument document, @NotNull final Route route, final MessageEmbed.Field field) {
        document.set(route, FieldSerializer.getMapFromField(field));
    }

    public static void setFields(@NotNull final YamlDocument document, @NotNull final Route route, final List<MessageEmbed.Field> fields) {
        List<LinkedHashMap<?, ?>> list = new ArrayList<>();
        for(MessageEmbed.Field field : fields) list.add(FieldSerializer.getMapFromField(field));

        document.set(route, list);
    }

    private static LinkedHashMap<?, ?> getMapFromField(final MessageEmbed.Field field) {
        String name;
        String value;
        boolean inline;

        if(field == null) {
            name = FieldSerializer.DEFAULT_FIELD.getName();
            value = FieldSerializer.DEFAULT_FIELD.getValue();
            inline = FieldSerializer.DEFAULT_FIELD.isInline();
        } else {
            name = field.getName();
            value = field.getValue();
            inline = field.isInline();

            if(name == null) name = FieldSerializer.DEFAULT_FIELD.getName();
            if(value == null) value = FieldSerializer.DEFAULT_FIELD.getValue();
        }

        String finalName = name;
        String finalValue = value;
        boolean finalInline = inline;
        return new LinkedHashMap<Object, Object>(){{
            put("name", finalName);
            put("value", finalValue);
            put("inline", finalInline);
        }};
    }
}
