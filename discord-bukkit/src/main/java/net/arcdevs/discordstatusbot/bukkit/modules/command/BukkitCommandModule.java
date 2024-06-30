package net.arcdevs.discordstatusbot.bukkit.modules.command;

import net.arcdevs.discordstatusbot.common.modules.command.CommandModule;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.process.PermissionReader;

public class BukkitCommandModule extends CommandModule {
    public BukkitCommandModule(@NotNull final CommandHandler commandHandler) {
        super(commandHandler);
    }

    @Override
    protected PermissionReader getPermissionReader() {
        return BukkitPermissionReader.INSTANCE;
    }

    @Override
    protected void sendMessage(@NotNull CommandActor actor, @NotNull Component message) {
        ((BukkitCommandActor) actor).audience().sendMessage(message);
    }
}
