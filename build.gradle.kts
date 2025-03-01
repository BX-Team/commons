plugins {
    `java-library`
    `maven-publish`
}

group = project.group
version = project.version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // Adventure module
    api("net.kyori:adventure-text-minimessage:4.18.0")
    api("net.kyori:adventure-text-serializer-legacy:4.18.0")

    // Bukkit module
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")

    // Folia module
    compileOnlyApi("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")

    // For all modules
    api("org.jetbrains:annotations:26.0.1")
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withSourcesJar()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.bxteam.org/releases/")

            if (project.version.toString().endsWith("-SNAPSHOT")) {
                url = uri("https://repo.bxteam.org/snapshots/")
            }
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
