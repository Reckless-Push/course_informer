import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val nettyVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val h2Version: String by project
val kotlinxDatetimeVersion: String by project

plugins {
    application
    kotlin("jvm") version "2.0.0-Beta1"
    kotlin("plugin.serialization").version("2.0.0-Beta1")
    id("io.ktor.plugin") version "3.0.0-beta-1"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.diffplug.spotless") version "6.23.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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

tasks.withType<ShadowJar> {
    archiveBaseName.set("course-informer")
    archiveVersion.set("0.0.1")
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

group = "edu.umass"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven/") }
}

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-hsts:$ktorVersion")
    implementation("io.ktor:ktor-server-http-redirect:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
