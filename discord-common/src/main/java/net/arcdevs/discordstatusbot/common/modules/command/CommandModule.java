package net.arcdevs.discordstatusbot.common.modules.command;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.exception.NumberNotInRangeException;
import revxrsal.commands.help.CommandHelpWriter;
import revxrsal.commands.process.PermissionReader;

@Getter(AccessLevel.PROTECTED)
public abstract class CommandModule extends DiscordModule {
    protected abstract PermissionReader getPermissionReader();
    protected abstract void sendMessage(@NotNull CommandActor actor, @NotNull Component message);

    private final CommandHandler commandHandler;

    public CommandModule(@NotNull final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    protected void enable() {
        this.commandHandler.registerContextResolver(CommandModule.class, (context) -> this);
        this.commandHandler.registerContextResolver(CommandHelp.class, (context) -> {
            if (this.commandHandler.getHelpWriter() == null)
                throw new IllegalArgumentException("No help writer is registered.");
            ExecutableCommand helpCommand = context.command();
            CommandHelpWriter<?> writer = this.commandHandler.getHelpWriter();
            CommandHelp<Object> entries = new CommandHelp<>();
            CommandCategory parent = helpCommand.getParent();
            CommandPath parentPath = parent == null ? null : parent.getPath();
            this.commandHandler.getCommands().values().stream().sorted().forEach(command -> {
                if (parentPath == null || parentPath.isParentOf(command.getPath())) {
                    Object generated = writer.generate(command, context.actor());
                    if (generated != null) entries.add(generated);
                }
            });
            return entries;
        });
        this.commandHandler.registerExceptionHandler(NumberNotInRangeException.class, (actor, exception) -> {
            String parameter = exception.getParameter().getName();
            this.sendMessage(actor, MiniMessage.miniMessage().deserialize(String.format("<red>%s must be between %s and %s (found %s).", parameter.substring(0, 1).toUpperCase() + parameter.substring(1), (int) exception.getMinimum(), (int) exception.getMaximum(), exception.getInput())));
        });
        this.commandHandler.registerPermissionReader(this.getPermissionReader());
        this.commandHandler.setHelpWriter((command, actor) -> {
            String commandPath = command.getPath().toRealString();

            StringBuilder builder = new StringBuilder(String.format("  <gray>→</gray><hover:show_text:'<gray>→</gray> /%s'><click:suggest_command:/%s> /%s", commandPath, commandPath, commandPath));
            if(!StringUtils.isBlank(command.getUsage())) builder.append(String.format(" <aqua>%s</aqua>", command.getUsage()));

            builder.append(String.format(" <dark_gray>-</dark_gray> %s</click></hover>", command.getDescription()));

            return builder.toString();
        });

        this.commandHandler.register(new Commands());
    }

    @Override
    protected void disable() {

    }

    @Override
    protected void reload() {

    }
}
