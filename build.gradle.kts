plugins {
    java
    alias(libs.plugins.shadow)
}

allprojects {
    repositories {
        mavenCentral()

        maven(url = "https://jitpack.io/") // Lamp, Simple-YAML
        maven(url = "https://repo.william278.net/releases/") // PAPIProxyBridge
    }

    apply(plugin = "java")
    apply(plugin = "io.github.goooler.shadow")

    dependencies {
        compileOnly(rootProject.libs.lamp.common)

        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)

        compileOnly(rootProject.libs.annotations)
        compileOnly(rootProject.libs.simple.yaml) {
            exclude(group = "org.yaml")
        }

        implementation(rootProject.libs.jda) {
            exclude(module="opus-java")
        }

        implementation(rootProject.libs.placeholder.proxy)
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            archiveFileName.set("${rootProject.name}-${project.name.replaceFirstChar { it.uppercase() }}-${rootProject.version}.jar")

            isPreserveFileTimestamps = false

            val prefix = rootProject.group.toString() + ".libs"
            relocate("org.bstats", "$prefix.bstats")
            relocate("com.google.gson", "$prefix.gson")
            relocate("revxrsal", "$prefix.lamp")
            relocate("com.simpleyaml", "$prefix.simpleyaml")
            relocate("net.william278", "$prefix.papiproxybridge")

             exclude("META-INF/*/**")

            minimize {
                exclude(project(":api"))
            }
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
        dependsOn(listOf("api", "common", "bukkit", "bungee", "velocity").flatMap { listOf("$it:clean", "$it:shadowJar") })
    }
}