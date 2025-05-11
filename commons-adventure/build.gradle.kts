plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(project(":commons-shared"))
    api(libs.adventure.text.minimessage)
    api(libs.adventure.text.serializer.legacy)
}
