import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    alias(libs.plugins.plugin.yml.bukkit) apply(true)
}

bukkit {
    name = rootProject.name
    version = rootProject.version.toString()
    description = rootProject.description

    main = rootProject.group.toString() + ".bukkit.DiscordBukkitPlugin"

    foliaSupported = true

    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    softDepend = listOf("PlaceholderAPI")
}

repositories {
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    compileOnly(rootProject.libs.spigot)

    implementation(rootProject.libs.bstats.bukkit)
    implementation(rootProject.libs.lamp.bukkit)
    implementation(rootProject.libs.placeholder.api)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8