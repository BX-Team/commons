plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api("org.jetbrains:annotations:26.0.1")

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}
