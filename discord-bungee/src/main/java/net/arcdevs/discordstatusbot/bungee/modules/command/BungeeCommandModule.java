package net.arcdevs.discordstatusbot.bungee.modules.command;

import net.arcdevs.discordstatusbot.common.modules.command.CommandModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.process.PermissionReader;

public class BungeeCommandModule extends CommandModule {
    public BungeeCommandModule(@NotNull final CommandHandler commandHandler) {
        super(commandHandler);
    }

    @Override
    protected PermissionReader getPermissionReader() {
        return BungeePermissionReader.INSTANCE;
    }

    @Override
    protected void sendMessage(@NotNull CommandActor actor, @NotNull Component message) {
        actor.reply(PlainTextComponentSerializer.plainText().serialize(message));
    }
}
