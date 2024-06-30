plugins {
    java
    alias(libs.plugins.shadow)
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.github.goooler.shadow")

    repositories {
        mavenCentral() // Annotations, Lombok

        maven(url = "https://jitpack.io/") // Lamp
        // maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") // libby
    }

    dependencies {
        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)

        compileOnly(rootProject.libs.annotations)

        implementation(rootProject.libs.adventure.legacy)
        implementation(rootProject.libs.adventure.minimessage)
        implementation(rootProject.libs.adventure.plain)
        implementation(rootProject.libs.boosted.yaml)
        implementation(rootProject.libs.commons.lang3)
        implementation(rootProject.libs.lamp.common)
    }

    tasks {
        shadowJar {
            archiveFileName.set("${rootProject.name}-${project.name.replaceFirstChar { it.uppercase() }}-${rootProject.version}.jar")

            isPreserveFileTimestamps = false

            val prefix = rootProject.group.toString() + ".libs"
            relocate("org.bstats", "$prefix.bstats")
            relocate("com.google.gson", "$prefix.gson")
            relocate("revxrsal", "$prefix.lamp")
            // relocate("com.alessiodp.libby", "$prefix.libby")

            exclude("META-INF/*/**")

            minimize()
        }

        compileJava {
            options.compilerArgs.addAll(listOf("-Xlint:-options", "-parameters"))
            options.encoding = "UTF-8"
        }

        jar {
            manifest {
                attributes["Implementation-Version"] = rootProject.version
            }
        }
    }
}

tasks {
    register("build-discord") {
        dependsOn(listOf("common", "bukkit", "bungee", "velocity").flatMap { listOf("$it:clean", "$it:shadowJar") })
    }
}