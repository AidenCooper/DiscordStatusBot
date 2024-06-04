package net.arcdevs.discordstatusbot.command;

import net.arcdevs.discordstatusbot.MainPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.*;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

import java.io.IOException;

@Command({"dsb", "discordstatusbot"})
public class MinecraftCommand {
    @NotNull
    private final MainPlugin plugin;

    public MinecraftCommand(@NotNull MainPlugin plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("~")
    @CommandPermission("dsb.help")
    public void defaultCommand(CommandSender sender, CommandHelp<String> entries, @Named("page") @Range(min = 1, max = 1) @Default("1") int page) {
        this.helpCommand(sender, entries, page);
    }

    @Subcommand("help")
    @CommandPermission("dsb.help")
    @Description("Sends this help message.")
    public void helpCommand(CommandSender sender, CommandHelp<String> entries, @Named("page") @Range(min = 1, max = 1) @Default("1") int page) {
        final int pageSize = 8;
        final int maxPages = entries.getPageSize(pageSize);

        final CommandHelp<String> entryList = entries.paginate(page, pageSize);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                                                 "));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&bPage &7(%s/%s)", page, maxPages)));
        sender.sendMessage("");
        for(String entry : entryList) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', entry));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m                                                                                 "));
    }

    @Subcommand("reload")
    @CommandPermission("dsb.reload")
    @Description("Reloads the config files.")
    public String reloadCommand() throws CommandErrorException {
        try {
            this.plugin.getClientConfig().reload();
            this.plugin.getDataConfig().reload();
            this.plugin.getMessageConfig().reload();
        } catch (IOException exception) { return "&cReload failed."; }

        this.plugin.getDiscordClient().reload();

        return "&aReload complete.";
    }
}
