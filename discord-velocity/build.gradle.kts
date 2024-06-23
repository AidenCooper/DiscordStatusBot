repositories {
    maven(url = "https://repo.papermc.io/repository/maven-public/") // Velocity
    maven(url = "https://repo.william278.net/releases/") // PAPIProxyBridge
}

dependencies {
    implementation(project(":common"))

    compileOnly(rootProject.libs.velocity)
    annotationProcessor(rootProject.libs.velocity)

    implementation(rootProject.libs.bstats.velocity)
    implementation(rootProject.libs.lamp.velocity)
    implementation(rootProject.libs.placeholder.proxy)
}

tasks {
    processResources {
        val props = mapOf(
            "version" to rootProject.version.toString().split("-")[0],
            "description" to rootProject.description,
            "main" to rootProject.group.toString() + ".velocity.VelocityPlugin"
        )

        inputs.properties(props)
        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }

    shadowJar {
        dependencies {
            exclude(dependency(rootProject.libs.placeholder.proxy.get()))
        }
    }
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17