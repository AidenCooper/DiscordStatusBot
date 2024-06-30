package net.arcdevs.discordstatusbot.bungee.modules.command;

import net.arcdevs.discordstatusbot.common.modules.command.CommandPermission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.bungee.BungeeCommandPermission;
import revxrsal.commands.command.trait.CommandAnnotationHolder;
import revxrsal.commands.process.PermissionReader;

public enum BungeePermissionReader implements PermissionReader {
    INSTANCE;

    @Override
    public @Nullable revxrsal.commands.command.CommandPermission getPermission(@NotNull CommandAnnotationHolder command) {
        CommandPermission permissionAnn = command.getAnnotation(CommandPermission.class);
        if (permissionAnn == null)
            return null;
        return new BungeeCommandPermission(permissionAnn.value());
    }
}
