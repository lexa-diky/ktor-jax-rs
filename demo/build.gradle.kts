plugins {
    kotlin("jvm") version "1.9.21"
    alias(libs.plugins.ksp)
}

group = "io.github.lexadiky.ktor-jax-rs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.runtime)
    ksp(projects.kspProcessor)

    implementation(libs.jakarta.ws.rs.api)
    implementation(libs.logback)
    implementation(libs.ktor.engine.netty)
    implementation(libs.ktor.plugin.serialization.json)
    implementation(libs.ktor.plugin.contentNegotiation)
    implementation(libs.ktor.plugin.logging)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}