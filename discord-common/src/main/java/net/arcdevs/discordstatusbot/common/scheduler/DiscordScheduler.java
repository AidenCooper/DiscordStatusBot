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

        return Executors.newSingleThreadScheduledExecutor(runnable -> {
            final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setName(name);
            thread.setDaemon(true);
            return thread;
        });
    }

    public void closeThread(@NotNull final String name) {
        ScheduledExecutorService service = this.getServices().get(name);
        if(service == null) return;

        this.getServices().remove(name).shutdown();
    }

    public void closeThreads() {
        for(String name : this.getServices().keySet()) {
            this.closeThread(name);
        }

        this.getServices().clear();
    }
}
