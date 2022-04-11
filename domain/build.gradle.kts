import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "me.leandro"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.12.3")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}