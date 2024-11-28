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
    api("net.kyori:adventure-text-minimessage:4.17.0")
    api("net.kyori:adventure-text-serializer-legacy:4.17.0")
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.bx-team.space/releases/")

            if (project.version.toString().endsWith("-SNAPSHOT")) {
                url = uri("https://repo.bx-team.space/snapshots/")
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
