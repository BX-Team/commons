plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(libs.annotations)
    implementation(libs.maven.artifact)

    compileOnly(libs.spigot)
    testImplementation(libs.spigot)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
