package net.arcdevs.discordstatusbot.common.modules.command;

import org.jetbrains.annotations.Range;
import revxrsal.commands.exception.InvalidHelpPageException;

import java.util.ArrayList;

public final class CommandHelp<T> extends ArrayList<T> implements revxrsal.commands.help.CommandHelp<T> {
    @Override
    public CommandHelp<T> paginate(int page, int elementsPerPage) throws InvalidHelpPageException {
        if (isEmpty()) return new CommandHelp<>();
        CommandHelp<T> list = new CommandHelp<>();
        int size = getPageSize(elementsPerPage);
        if (page > size)
            throw new InvalidHelpPageException(this, page, elementsPerPage);
        int listIndex = page - 1;
        int l = Math.min(page * elementsPerPage, size());
        for (int i = listIndex * elementsPerPage; i < l; ++i) {
            list.add(get(i));
        }
        return list;
    }

    @Override
    public @Range(from = 1L, to = Long.MAX_VALUE) int getPageSize(int elementsPerPage) {
        if (elementsPerPage < 1)
            throw new IllegalArgumentException("Elements per page cannot be less than 1! (Found " + elementsPerPage + ")");
        return (size() / elementsPerPage) + (size() % elementsPerPage == 0 ? 0 : 1);
    }
}
