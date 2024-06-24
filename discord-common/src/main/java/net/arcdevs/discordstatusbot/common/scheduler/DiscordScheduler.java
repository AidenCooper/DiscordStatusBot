package net.arcdevs.discordstatusbot.common.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter(AccessLevel.PRIVATE)
public class DiscordScheduler {
    private final HashMap<String, ScheduledExecutorService> services = new HashMap<>();

    public ScheduledExecutorService createThread(@NotNull final String name) {
        if(this.getServices().containsKey(name)) return this.getServices().get(name);

        try(ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(runnable -> {
            final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setName(name);
            thread.setDaemon(true);
            return thread;
        })) {
           return service;
        } catch (NullPointerException exception) {
            return null;
        }
    }

    public void closeThread(@NotNull final String name) {
        ScheduledExecutorService service = this.getServices().get(name);
        if(service == null) return;

        service.close();
        this.getServices().remove(name);
    }

    public void closeThreads() {
        for(ScheduledExecutorService service : this.getServices().values()) {
            service.close();
        }

        this.getServices().clear();
    }
}
