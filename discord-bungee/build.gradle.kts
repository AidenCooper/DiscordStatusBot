plugins {
    alias(libs.plugins.plugin.yml.bungee) apply(true)
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") // Bungee
}

dependencies {
    implementation(project(":common"))

    compileOnly(rootProject.libs.bungee)

    implementation(rootProject.libs.bstats.bungee)
    implementation(rootProject.libs.lamp.bungee)
}

bungee {
    name = rootProject.name
    version = rootProject.version.toString()
    description = rootProject.description
    author = rootProject.findProperty("author").toString()

    main = rootProject.group.toString() + ".bungee.BungeePlugin"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8