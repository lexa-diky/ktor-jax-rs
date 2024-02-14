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
    ksp(projects.kspProcessor)
    implementation(libs.jakarta.ws.rs.api)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}