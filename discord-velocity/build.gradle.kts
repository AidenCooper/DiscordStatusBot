repositories {
    maven(url = "https://repo.papermc.io/repository/maven-public/") // Velocity
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    compileOnly(rootProject.libs.velocity)
    annotationProcessor(rootProject.libs.velocity)

    implementation(rootProject.libs.bstats.velocity)
    implementation(rootProject.libs.lamp.velocity)
}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")
val generateTemplates = tasks.register<Copy>("generateTemplates") {
    val props = mapOf(
        "version" to rootProject.version,
        "description" to rootProject.description
    )

    inputs.properties(props)
    from(templateSource)
    into(templateDest)
    expand(props)
}
sourceSets {
    main {
        java {
            srcDirs(generateTemplates.map { it.outputs })
        }
    }
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17