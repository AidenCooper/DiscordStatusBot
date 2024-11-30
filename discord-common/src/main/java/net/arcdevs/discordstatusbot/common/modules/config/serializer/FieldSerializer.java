package net.arcdevs.discordstatusbot.common.modules.config.serializer;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FieldSerializer extends Serializer<MessageEmbed.Field> {
    public FieldSerializer() {
        super(new MessageEmbed.Field("", "", false));
    }

    @Override
    public MessageEmbed.Field get(@NotNull final YamlDocument document, @NotNull final Route route) {
        String name = document.getString(route + ".name");
        String value = document.getString(route + ".value");
        boolean inline = document.getBoolean(route + ".inline");

        if(name == null) name = this.getDefaultValue().getName();
        if(value == null) value = this.getDefaultValue().getValue();

        return new MessageEmbed.Field(name, value, inline);
    }

    @Override
    public List<MessageEmbed.Field> getList(@NotNull YamlDocument document, @NotNull Route route) {
        List<MessageEmbed.Field> fields = new ArrayList<>();

        document.getList(route).forEach(fieldObject -> {
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) fieldObject;

            if(map == null) {
                fields.add(this.getDefaultValue());
                return;
            }

            String name = (String) map.get("name");
            String value = (String) map.get("value");
            Boolean inline = (Boolean) map.get("inline");

            if(name == null) name = this.getDefaultValue().getName();
            if(value == null) value = this.getDefaultValue().getValue();
            if(inline == null) inline = this.getDefaultValue().isInline();

            fields.add(new MessageEmbed.Field(name, value, inline));
        });

        return fields;
    }

    @Override
    public void set(@NotNull final YamlDocument document, @NotNull final Route route, final MessageEmbed.Field field) {
        document.set(route, this.getMapFromField(field));
    }

    @Override
    public void setList(@NotNull YamlDocument document, @NotNull Route route, List<MessageEmbed.Field> fields) {
        List<LinkedHashMap<?, ?>> list = new ArrayList<>();
        for(MessageEmbed.Field field : fields) list.add(this.getMapFromField(field));

        document.set(route, list);
    }

    private LinkedHashMap<?, ?> getMapFromField(final MessageEmbed.Field field) {
        String name;
        String value;
        boolean inline;

        if(field == null) {
            name = this.getDefaultValue().getName();
            value = this.getDefaultValue().getValue();
            inline = this.getDefaultValue().isInline();
        } else {
            name = field.getName();
            value = field.getValue();
            inline = field.isInline();

            if(name == null) name = this.getDefaultValue().getName();
            if(value == null) value = this.getDefaultValue().getValue();
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
