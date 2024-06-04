package net.arcdevs.discordstatusbot.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FieldSerializer {
    public static MessageEmbed.Field getField(@NotNull YamlDocument document, Route route) {
        String name = document.getString(route + ".name");
        String value = document.getString(route + ".value");
        boolean inline = document.getBoolean(route + ".inline");

        if(name == null) name = "";
        if(value == null) value = "";

        return new MessageEmbed.Field(name, value, inline);
    }

    public static List<MessageEmbed.Field> getFields(@NotNull YamlDocument document, Route route) {
        List<MessageEmbed.Field> fields = new ArrayList<>();

        document.getList(route).forEach(fieldObject -> {
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) fieldObject;

            String name = (String) map.get("name");
            String value = (String) map.get("value");
            boolean inline = (Boolean) map.get("inline");

            if(name == null) name = "";
            if(value == null) value = "";

            fields.add(new MessageEmbed.Field(name, value, inline));
        });

        return fields;
    }

    public static void setField(@NotNull YamlDocument document, @NotNull Route route, @NotNull MessageEmbed.Field field) {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();

        String name = field.getName();
        String value = field.getValue();
        boolean inline = field.isInline();

        if(name == null) name = "";
        if(value == null) value = "";

        map.put("name", name);
        map.put("value", value);
        map.put("inline", inline);

        document.set(route, map);
    }

    public static void setFields(@NotNull YamlDocument document, @NotNull Route route, @NotNull List<MessageEmbed.Field> fields) {
        List<LinkedHashMap<Object, Object>> list = new ArrayList<>();
        for(MessageEmbed.Field field : fields) {
            LinkedHashMap<Object, Object> map = new LinkedHashMap<>();

            String name = field.getName();
            String value = field.getValue();
            boolean inline = field.isInline();

            if(name == null) name = "";
            if(value == null) value = "";

            map.put("name", name);
            map.put("value", value);
            map.put("inline", inline);

            list.add(map);
        }

        document.set(route, list);
    }
}
