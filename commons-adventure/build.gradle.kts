plugins {
    `commons-java`
    `commons-publish`
    `commons-repositories`
}

dependencies {
    api(project(":commons-shared"))
    api("net.kyori:adventure-text-minimessage:4.18.0")
    api("net.kyori:adventure-text-serializer-legacy:4.18.0")
}
