plugins {
    kotlin("jvm") version "2.2.20-RC"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.lokixcz"
version = "1.5.2"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    // For Vault API
    maven("https://jitpack.io")
}

// Configuration for an optional fully shaded Spigot artifact
configurations {
    create("spigotShade")
}

dependencies {
    // Target Paper API 1.20.1 for broad compatibility; runtime can be newer servers
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    // Keep Kotlin stdlib shaded to avoid runtime dependency mismatch
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Vault API (provided by Vault plugin at runtime)
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // SQLite JDBC (provided at runtime via Paper library loader; do not shade)
    compileOnly("org.xerial:sqlite-jdbc:3.46.0.0")
    // Web stack provided at runtime via Paper's library loader to keep our jar slim
    compileOnly("io.javalin:javalin:5.6.1")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    // Dependencies for the optional Spigot shaded jar (all-in-one)
    add("spigotShade", "io.javalin:javalin:5.6.1")
    add("spigotShade", "com.fasterxml.jackson.core:jackson-databind:2.15.2")
    add("spigotShade", "com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    add("spigotShade", "com.fasterxml.jackson.core:jackson-core:2.15.2")
    add("spigotShade", "com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    add("spigotShade", "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    add("spigotShade", "org.jetbrains.kotlin:kotlin-stdlib")
    add("spigotShade", "org.jetbrains.kotlin:kotlin-stdlib-common")
    // Include SQLite so Spigot can use SQLite mode without extra setup (increases size)
    add("spigotShade", "org.xerial:sqlite-jdbc:3.46.0.0")
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
    // Prevent Gradle from scanning destination dir (plugins/) which may contain
    // transient/unreadable files (e.g., SQLite -journal). See Gradle 8 docs.
    doNotTrackState("Destination contains runtime-generated files (e.g., market.db-journal)")
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

// Optional: produce an all-in-one Spigot jar with shaded dependencies
val shadowJarSpigot by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    archiveBaseName.set("TheEndex")
    archiveClassifier.set("spigot")
    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.getByName("spigotShade"))
    // Try to keep size reasonable by stripping unused classes
    minimize()
}

// Copy the spigot jar to release folder as well
val copySpigotToRelease by tasks.registering(Copy::class) {
    dependsOn(shadowJarSpigot)
    val jarFile = shadowJarSpigot.flatMap { it.archiveFile }
    from(jarFile)
    into(layout.projectDirectory.dir("release"))
}

tasks.build {
    finalizedBy(copySpigotToRelease)
}

// Copy Crypto addon jar to the test server's plugin data folder (TheEndex/addons)
val copyCryptoToTestServer by tasks.registering(Copy::class) {
    // Ensure crypto addon jar is built
    dependsOn(project(":addons:crypto").tasks.named("jar"))
    // Same as above: avoid scanning plugin data folder which can have transient files
    doNotTrackState("Destination contains runtime-generated files (plugin data)")
    // Grab the produced jar from the crypto addon subproject
    val cryptoJar = project(":addons:crypto").tasks.named<org.gradle.jvm.tasks.Jar>("jar").flatMap { it.archiveFile }
    from(cryptoJar)
    into(layout.projectDirectory.dir("MCTestServer/plugins/TheEndex/addons"))
}

// Copy Crypto addon jar to release/TheEndex/addons for distribution
val copyCryptoToRelease by tasks.registering(Copy::class) {
    dependsOn(project(":addons:crypto").tasks.named("jar"))
    val cryptoJar = project(":addons:crypto").tasks.named<org.gradle.jvm.tasks.Jar>("jar").flatMap { it.archiveFile }
    from(cryptoJar)
    into(layout.projectDirectory.dir("release/TheEndex/addons"))
}

tasks.build {
    finalizedBy(copyCryptoToTestServer)
    finalizedBy(copyCryptoToRelease)
}

// Convenience task to distribute only addon jars without building the main plugin
tasks.register("distributeAddons") {
    dependsOn(copyCryptoToTestServer)
    dependsOn(copyCryptoToRelease)
}
