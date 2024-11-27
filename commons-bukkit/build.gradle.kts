plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api("org.jetbrains:annotations:24.1.0")

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    testImplementation("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
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
