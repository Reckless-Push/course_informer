val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

val exposedVersion: String by project
val h2Version: String by project

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
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
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-hsts-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-http-redirect-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
}
