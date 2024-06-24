package net.arcdevs.discordstatusbot.common.modules.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum ConfigName {
    CLIENT("client.yml"),
    DATA("data.yml"),
    MESSAGE("message.yml");

    @NotNull private final String name;

    @Override
    public String toString() {
        return this.getName();
    }
}
