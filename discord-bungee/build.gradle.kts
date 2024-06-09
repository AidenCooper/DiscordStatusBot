plugins {
    alias(libs.plugins.plugin.yml.bungee) apply(true)
}

bungee {
    name = rootProject.name
    version = rootProject.version.toString()
    description = rootProject.description

    main = rootProject.group.toString() + ".bungee.DiscordBungeePlugin"

    softDepends = setOf("PlaceholderAPI")
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") // Bungee
    maven(url = "https://repo.william278.net/releases/") // PAPIProxyBridge
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    compileOnly(rootProject.libs.bungee)

    implementation(rootProject.libs.bstats.bungee)
    implementation(rootProject.libs.lamp.bungee)
    implementation(rootProject.libs.placeholder.proxy)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8