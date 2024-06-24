package net.arcdevs.discordstatusbot.common.exceptions;

import lombok.Getter;

@Getter
public final class ModuleException extends RuntimeException {
    private final boolean required;

    public ModuleException(boolean required) {
        super();

        this.required = required;
    }

    public ModuleException(String message, boolean required) {
        super(message);

        this.required = required;
    }

    public ModuleException(String message, Throwable throwable, boolean required) {
        super(message, throwable);

        this.required = required;
    }
}
