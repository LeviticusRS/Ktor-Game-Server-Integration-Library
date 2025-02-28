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
    "ktor.version" to "3.0.3"
)

dependencies {
    implementation("io.ktor:ktor-client-json-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-core-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-logging-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-cio-jvm:${versions["ktor.version"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.1")
    implementation("io.ktor:ktor-http-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-serialization-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-serialization-jackson-jvm:${versions["ktor.version"]}")
    implementation("io.ktor:ktor-client-okhttp:${versions["ktor.version"]}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
}


group = "com.rsps"
version = "1.0.0"
description = "event"
java.sourceCompatibility = JavaVersion.VERSION_17
