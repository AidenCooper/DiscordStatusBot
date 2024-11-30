repositories {
    maven(url = "https://repo.papermc.io/repository/maven-public/") // Velocity
}

dependencies {
    implementation(project(":common"))

    compileOnly(rootProject.libs.velocity)
    annotationProcessor(rootProject.libs.velocity)

    implementation(rootProject.libs.bstats.velocity)
    implementation(rootProject.libs.lamp.velocity)
}

tasks {
    processResources {
        val props = mapOf(
            "version" to rootProject.version.toString().split("-")[0],
            "description" to rootProject.description,
            "main" to rootProject.group.toString() + ".velocity.VelocityPlugin",
            "author" to rootProject.findProperty("author").toString()
        )

        inputs.properties(props)
        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17