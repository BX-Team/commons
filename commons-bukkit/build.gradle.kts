plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(project(":commons-shared"))
    api(libs.annotations)

    compileOnly(libs.spigot)
    testImplementation(libs.spigot)
}
