package net.arcdevs.discordserverstatusupdater.commands;

import net.arcdevs.discordserverstatusupdater.MainPlugin;
import net.arcdevs.discordserverstatusupdater.bot.ServerStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "/dssu reload");
        } else {
            MainPlugin.INSTANCE.getMainConfig().reload();

            MainPlugin.INSTANCE.getDiscordBot().setChannelID(MainPlugin.INSTANCE.getMainConfig().getDiscordChannelID());
            MainPlugin.INSTANCE.getDiscordBot().setMessageID(MainPlugin.INSTANCE.getMainConfig().getDiscordMessageID());

            MainPlugin.INSTANCE.getDiscordBot().updateStatusMessage(ServerStatus.ONLINE);

            sender.sendMessage(ChatColor.AQUA + "Discord Server Status Updater " + ChatColor.GRAY + "has been reloaded!");
        }
        return false;
    }
}
