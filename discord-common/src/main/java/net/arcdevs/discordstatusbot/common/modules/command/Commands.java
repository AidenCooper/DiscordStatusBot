package net.arcdevs.discordstatusbot.common.modules.command;

import net.arcdevs.discordstatusbot.common.Discord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.*;
import revxrsal.commands.command.CommandActor;

@Command("dsb")
public class Commands {
    @DefaultFor("~")
    @CommandPermission("dsb.help")
    public void defaultCommand(CommandModule module, CommandActor actor, CommandHelp<String> entries, @Named("page") @Range(min = 1, max = 1) @Default("1") int page) {
        this.helpCommand(module, actor, entries, page);
    }

    @Subcommand("help")
    @CommandPermission("dsb.help")
    @Description("Sends this help message.")
    public void helpCommand(CommandModule module, CommandActor actor, CommandHelp<String> entries, @Named("page") @Range(min = 1, max = 1) @Default("1") int page) {
        int pageSize = 8;
        int maxPages = entries.getPageSize(pageSize);
        CommandHelp<String> entryList = entries.paginate(page, pageSize);

        module.sendMessage(actor, MiniMessage.miniMessage().deserialize("<gray><st>                                                                                 </st>"));
        module.sendMessage(actor, MiniMessage.miniMessage().deserialize(String.format("<aqua>Page <gray>(%s/%s)", page, maxPages)));
        module.sendMessage(actor, Component.text(""));
        for(String entry : entryList) module.sendMessage(actor, MiniMessage.miniMessage().deserialize(entry));
        module.sendMessage(actor, MiniMessage.miniMessage().deserialize("<gray><st>                                                                                 </st>"));
    }

    @Subcommand("reload")
    @CommandPermission("dsb.reload")
    @Description("Reloads all built-in modules.")
    public void reloadCommand(CommandModule module, @NotNull CommandActor actor) {
        try {
            Discord.get().getModuleManager().reload();
        } catch (Throwable throwable) {

            module.sendMessage(actor, MiniMessage.miniMessage().deserialize("<red>Reload failed."));
            Discord.get().getLogger().severe("Error reloading modules.\n" + throwable.getLocalizedMessage());
            return;
        }

        module.sendMessage(actor, MiniMessage.miniMessage().deserialize("<green>Reload complete."));
    }
}
