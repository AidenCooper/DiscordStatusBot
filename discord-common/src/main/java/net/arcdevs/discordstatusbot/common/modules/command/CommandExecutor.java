package net.arcdevs.discordstatusbot.common.modules.command;

import revxrsal.commands.annotation.*;
import revxrsal.commands.command.CommandActor;

public class CommandExecutor {
    @DefaultFor("~")
    public void defaultCommand() {

    }

    @Subcommand("help")
    @Description("Sends this help message.")
    public void helpCommand(CommandActor actor, CommandHelp<String> entries, @Named("page") @Range(min = 1, max = 1) @Default("1") int page) {

    }

    @Subcommand("reload")
    @Description("Reloads the config files.")
    public String reloadCommand() {
        return "&aReload complete.";
    }
}
