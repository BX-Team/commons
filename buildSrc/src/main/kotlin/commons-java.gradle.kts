import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

plugins {
    `java-library`
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
