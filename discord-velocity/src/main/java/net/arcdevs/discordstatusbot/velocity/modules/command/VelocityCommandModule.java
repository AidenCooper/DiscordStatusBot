package net.arcdevs.discordstatusbot.velocity.modules.command;

import net.arcdevs.discordstatusbot.common.modules.command.CommandModule;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.process.PermissionReader;
import revxrsal.commands.velocity.VelocityCommandActor;
import revxrsal.commands.velocity.VelocityCommandHandler;

public class VelocityCommandModule extends CommandModule {
    public VelocityCommandModule(@NotNull final VelocityCommandHandler commandHandler) {
        super(commandHandler);
    }


    @Override
    protected PermissionReader getPermissionReader() {
        return VelocityPermissionReader.INSTANCE;
    }

    @Override
    protected void sendMessage(@NotNull CommandActor actor, @NotNull Component message) {
        ((VelocityCommandActor) actor).reply(message);
    }
}
