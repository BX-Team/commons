plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(project(":commons-shared"))
    api("org.jetbrains:annotations:26.0.1")

    compileOnlyApi("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")
}
