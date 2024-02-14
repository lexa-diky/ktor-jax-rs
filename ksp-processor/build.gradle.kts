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
    implementation(libs.jakarta.ws.rs.api)
    implementation(libs.autoservice.annotations)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.ksp.api)
    ksp(libs.autoservice.ksp)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}