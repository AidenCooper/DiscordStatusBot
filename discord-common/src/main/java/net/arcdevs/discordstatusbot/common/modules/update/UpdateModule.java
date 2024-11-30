package net.arcdevs.discordstatusbot.common.modules.update;

import lombok.AccessLevel;
import lombok.Getter;
import net.arcdevs.discordstatusbot.common.Discord;
import net.arcdevs.discordstatusbot.common.modules.DiscordModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PRIVATE)
public class UpdateModule extends DiscordModule {
    private final int resourceID;

    public UpdateModule(int resourceID) {
        this.resourceID = resourceID;
    }

    @Override
    protected void enable() {
        this.check();
    }

    @Override
    protected void disable() {

    }

    @Override
    protected void reload() {

    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    private void check() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceID + "/").openStream(); Scanner scanner = new Scanner(is)) {
                if (scanner.hasNext()) {
                    String currentVersion = this.getClass().getPackage().getImplementationVersion();
                    String updateVersion = scanner.next();

                    if(currentVersion.equals(updateVersion)) {
                        Discord.get().getLogger().info("No new updates available.");
                    } else {
                        Discord.get().getLogger().warn((String.format("There is a new update available. v%s -> v%s.", currentVersion, updateVersion)));
                    }
                }
            } catch (IOException exception) {
                Discord.get().getLogger().info("Unable to check for updates.");
            }
        }, 0L, TimeUnit.SECONDS);
    }
}
