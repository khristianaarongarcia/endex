plugins {
    kotlin("jvm") version "2.2.20-RC"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.lokixcz"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    // For Vault API
    maven("https://jitpack.io")
}

dependencies {
    // Target Paper API 1.20.1 for broad compatibility; runtime can be newer servers
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Vault API (provided by Vault plugin at runtime)
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // SQLite JDBC (provided at runtime via Paper library loader; do not shade)
    compileOnly("org.xerial:sqlite-jdbc:3.46.0.0")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20.1")
    }
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

// Configure Shadow to not append a classifier, so final jar name is consistent
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("TheEndex")
    archiveClassifier.set("")
}

// After building, copy jar into local test server plugins folder, overwriting existing jar
val copyToTestServer by tasks.registering(Copy::class) {
    dependsOn(tasks.named("shadowJar"))
    val jarFile = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar").flatMap { it.archiveFile }
    from(jarFile)
    into(layout.projectDirectory.dir("MCTestServer/plugins"))
}

tasks.build {
    finalizedBy(copyToTestServer)
}

// Also copy the release jar into a top-level release/ folder for publishing
val copyToRelease by tasks.registering(Copy::class) {
    dependsOn(tasks.named("shadowJar"))
    val jarFile = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar").flatMap { it.archiveFile }
    from(jarFile)
    into(layout.projectDirectory.dir("release"))
}

tasks.build {
    finalizedBy(copyToRelease)
}
