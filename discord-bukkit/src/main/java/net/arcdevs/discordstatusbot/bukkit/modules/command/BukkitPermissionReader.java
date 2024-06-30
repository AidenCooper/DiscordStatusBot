package net.arcdevs.discordstatusbot.bukkit.modules.command;

import net.arcdevs.discordstatusbot.common.modules.command.CommandPermission;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.bukkit.BukkitCommandPermission;
import revxrsal.commands.command.trait.CommandAnnotationHolder;
import revxrsal.commands.process.PermissionReader;

public enum BukkitPermissionReader implements PermissionReader {
    INSTANCE;

    @Override
    public @Nullable revxrsal.commands.command.CommandPermission getPermission(@NotNull CommandAnnotationHolder command) {
        CommandPermission permissionAnn = command.getAnnotation(CommandPermission.class);
        if (permissionAnn == null)
            return null;
        return new BukkitCommandPermission(new Permission(permissionAnn.value(), PermissionDefault.OP));
    }
}
