import Build_gradle.Versions.exposed
import Build_gradle.Versions.fuel
import Build_gradle.Versions.javaJwt
import Build_gradle.Versions.javalin
import Build_gradle.Versions.koin
import Build_gradle.Versions.mockk
import Build_gradle.Versions.pgjdbc
import Build_gradle.Versions.slf4j
import Build_gradle.Versions.springSecurity
import Build_gradle.Versions.upsert
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`
}

group = "me.leandro"
version = "1.0-SNAPSHOT"

dependencies {

    // Domain
    implementation(project(":domain"))

    // Koin
    implementation("io.insert-koin:koin-core:$koin")
    testImplementation("io.insert-koin:koin-test:$koin")
    testImplementation("io.insert-koin:koin-test-junit4:$koin")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed")
    implementation("com.impossibl.pgjdbc-ng:pgjdbc-ng:$pgjdbc")
    implementation("pw.forst:exposed-upsert:$upsert")

    // Fuel
    implementation("com.github.kittinunf.fuel:fuel:$fuel")
    implementation("com.github.kittinunf.fuel:fuel-kotlinx-serialization:$fuel")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Javalin
    implementation("io.javalin:javalin:$javalin")
    implementation("io.javalin:javalin-openapi:$javalin")

    // SLF4J
    implementation("org.slf4j:slf4j-simple:$slf4j")

    // Spring Security
    implementation("org.springframework.security:spring-security-core:${springSecurity}")

    // Java JWT
    implementation("com.auth0:java-jwt:$javaJwt")

    // JUnit
    testImplementation(kotlin("test-junit"))

    // Mockk
    testImplementation("io.mockk:mockk:$mockk")

}

object Versions {
    const val koin = "3.1.5"
    const val exposed = "0.37.3"
    const val pgjdbc = "0.8.3"
    const val fuel = "2.3.1"
    const val javalin = "4.4.0"
    const val slf4j = "1.7.31"
    const val mockk = "1.12.3"
    const val springSecurity = "5.6.2"
    const val javaJwt = "3.19.1"
    const val upsert = "1.1.0"
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}