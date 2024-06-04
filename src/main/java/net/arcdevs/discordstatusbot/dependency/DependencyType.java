package net.arcdevs.discordstatusbot.dependency;

import org.jetbrains.annotations.NotNull;

public enum DependencyType {
    PLACEHOLDERAPI("PlaceholderAPI", false);

    @NotNull
    final String name;
    final boolean required;

    DependencyType(@NotNull final String name, final boolean required) {
        this.name = name;
        this.required = required;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public boolean isRequired() {
        return this.required;
    }
}
