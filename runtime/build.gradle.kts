plugins {
    kotlin("jvm") version "1.9.21"
    alias(libs.plugins.ksp)
    `maven-publish`
}

group = "io.github.lexadiky.ktor-jax-rs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jakarta.ws.rs.api)
    implementation(libs.autoservice.annotations)
    ksp(libs.autoservice.ksp)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(project.components["java"])
            groupId = "io.github.lexa-diky.ktor-jax-rs"
            artifactId = "runtime"
            version = "0.0.5-SNAPSHOT"
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = System.getenv("MAVEN_CENTRAL_USERNAME")
                password = System.getenv("MAVEN_CENTRAL_PASSWORD")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}