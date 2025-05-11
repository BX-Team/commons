plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(project(":commons-shared"))
    api(project(":commons-bukkit"))

    compileOnlyApi(libs.folia)
}
