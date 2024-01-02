import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val nettyVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val h2Version: String by project
val kotlinxDatetimeVersion: String by project
val postgresVersion: String by project
val junitVersion: String by project
val slf4jApiVersion: String by project
val kotlinxSerializationVersion: String by project
val kotlinxCoroutinesVersion: String by project

plugins {
    application
    kotlin("jvm") version "2.0.0-Beta2"
    kotlin("plugin.serialization").version("2.0.0-Beta2")
    id("io.ktor.plugin") version "3.0.0-beta-1"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.diffplug.spotless") version "6.23.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.autonomousapps.dependency-analysis") version "1.28.0"
}

extensions.getByType<com.diffplug.gradle.spotless.SpotlessExtension>().apply {
    kotlin {
        target("src/**/*.kt", "src/**/*.kts")
        ktlint()
        ktfmt()
        diktat().configFile("/build/diktat-analysis.yml")
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

tasks.register<Copy>("copyFrontEnd") {
    from(layout.buildDirectory.dir("../my-app/out"))
    into(layout.buildDirectory.dir("../src/main/resources/static"))
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
    runtimeOnly("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    runtimeOnly("com.h2database:h2:$h2Version")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresVersion")
    implementation("io.ktor:ktor-http:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-utils:$ktorVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.slf4j:slf4j-api:$slf4jApiVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}
