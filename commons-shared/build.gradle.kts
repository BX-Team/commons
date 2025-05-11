plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api("org.jetbrains:annotations:26.0.2")
    implementation("org.apache.maven:maven-artifact:3.9.9")

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
