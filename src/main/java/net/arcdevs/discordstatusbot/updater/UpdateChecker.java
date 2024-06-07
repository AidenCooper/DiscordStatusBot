package net.arcdevs.discordstatusbot.updater;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    @NotNull final JavaPlugin plugin;
    final int resourceID;

    public UpdateChecker(@NotNull final JavaPlugin plugin, final int resourceID) {
        this.plugin = plugin;
        this.resourceID = resourceID;
    }

    public void checkForUpdates() {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceID + "/").openStream(); Scanner scanner = new Scanner(is)) {
                if (scanner.hasNext()) {
                    String currentVersion = this.plugin.getDescription().getVersion();
                    String updateVersion = scanner.next();

                    if(currentVersion.equals(updateVersion)) {
                        this.plugin.getLogger().info("No new updates available.");
                    } else {
                        this.plugin.getLogger().warning((String.format("There is a new update available. v%s -> v%s.", currentVersion, updateVersion)));
                    }
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Unable to check for updates.");
            }
        });
    }
}
