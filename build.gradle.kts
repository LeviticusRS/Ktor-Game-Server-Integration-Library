plugins {
    id("java")
    kotlin("jvm")
}

repositories {
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

val versions = mapOf(
    "ktor.version" to "2.3.4"
)

dependencies {
    implementation("io.ktor:ktor-client-json-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-core-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-logging-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-cio-jvm:${versions["ktor.version"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.2")
    implementation("io.ktor:ktor-http-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-serialization-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-serialization-jackson-jvm:${versions["ktor.version"]}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
}


group = "com.rsps"
version = "1.0.0"
description = "event"
java.sourceCompatibility = JavaVersion.VERSION_17
