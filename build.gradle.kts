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
        compileOnly(rootProject.libs.lamp.common)

        implementation(rootProject.libs.boosted.yaml)
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
            options.compilerArgs.add("-Xlint:-options")
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