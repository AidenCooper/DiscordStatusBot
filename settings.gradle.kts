rootProject.name = "DiscordStatusBot"

sequenceOf("api", "common", "bukkit", "bungee", "velocity").forEach {
    val path = "discord-$it"
    val project = ":$it"

    include(project)
    project(project).projectDir = file(path)
}