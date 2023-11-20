val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

val exposedVersion: String by project
val h2Version: String by project

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.diffplug.spotless") version "6.22.0"
}

extensions.getByType<com.diffplug.gradle.spotless.SpotlessExtension>().apply {
    kotlin {
        target("src/**/*.kt", "src/**/*.kts")
        ktlint()
        ktfmt()
        diktat()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("../documentation/html"))
}

group = "edu.umass"
version = "0.0.1"

application {
    mainClass.set("edu.umass.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.6")
    implementation("io.ktor:ktor-client-core-jvm:2.3.6")
    implementation("io.ktor:ktor-client-apache-jvm:2.3.6")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.6")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.6")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.6")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.6")
    implementation("io.ktor:ktor-server-hsts-jvm:2.3.6")
    implementation("io.ktor:ktor-server-http-redirect-jvm:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.6")
    implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.6")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
}
