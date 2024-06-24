package net.arcdevs.discordstatusbot.common.modules.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public class ClientModule {
    private final ScheduledExecutorService UPDATER_SERVICE = Executors.newSingleThreadScheduledExecutor(runnable -> {
        final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("discord-updater");
        thread.setDaemon(true);
        return thread;
    });
    private final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", Pattern.CASE_INSENSITIVE);

    public ClientModule() {
    }
}
