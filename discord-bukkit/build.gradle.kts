import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    alias(libs.plugins.plugin.yml.bukkit) apply(true)
}

repositories {
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    implementation(project(":common"))

    compileOnly(rootProject.libs.spigot)

    implementation(rootProject.libs.bstats.bukkit)
    implementation(rootProject.libs.lamp.bukkit)
    implementation(rootProject.libs.placeholder.api)
}

bukkit {
    name = rootProject.name
    version = rootProject.version.toString()
    description = rootProject.description

    main = rootProject.group.toString() + ".bukkit.BukkitPlugin"

    foliaSupported = true

    apiVersion = "1.13"

    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    softDepend = listOf("PlaceholderAPI")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8