plugins {
    kotlin("jvm") version "2.2.20-RC"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.lokixcz"
version = "1.5.7-dec1022"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    // For Vault API
    maven("https://jitpack.io")
    // For PlaceholderAPI
    maven("https://repo.extendedclip.com/releases/") {
        name = "placeholderapi"
    }
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
    // bStats for plugin metrics
    implementation("org.bstats:bstats-bukkit:3.1.0")
    // Vault API (provided by Vault plugin at runtime)
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // PlaceholderAPI (provided by PlaceholderAPI plugin at runtime)
    compileOnly("me.clip:placeholderapi:2.11.6")
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
    // bStats for plugin metrics
    add("spigotShade", "org.bstats:bstats-bukkit:3.1.0")
    // SLF4J simple logger for Javalin on hybrid servers (Arclight/Mohist/etc)
    add("spigotShade", "org.slf4j:slf4j-simple:2.0.7")
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
    // Relocate bStats to avoid conflicts with other plugins
    relocate("org.bstats", "org.lokixcz.theendex.bstats")
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

// Process resources for Spigot build - copy plugin-spigot.yml as plugin.yml with version expansion
val processSpigotResources by tasks.registering(Copy::class) {
    from("src/main/resources") {
        exclude("plugin.yml")
        exclude("plugin-spigot.yml")
    }
    from("src/main/resources/plugin-spigot.yml") {
        rename { "plugin.yml" }
    }
    into(layout.buildDirectory.dir("spigot-resources"))
    // Expand version - match on SOURCE filename before rename
    filesMatching("plugin-spigot.yml") {
        expand("version" to project.version)
    }
}

// Optional: produce an all-in-one Spigot jar with shaded dependencies
val shadowJarSpigot by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    dependsOn(processSpigotResources)
    archiveBaseName.set("TheEndex")
    archiveClassifier.set("spigot")
    // Include compiled classes
    from(sourceSets.main.get().output.classesDirs)
    // Include processed resources (with plugin-spigot.yml renamed to plugin.yml)
    from(layout.buildDirectory.dir("spigot-resources"))
    configurations = listOf(project.configurations.getByName("spigotShade"))
    // Relocate bStats to avoid conflicts with other plugins
    relocate("org.bstats", "org.lokixcz.theendex.bstats")
    // Merge service files for Jetty/SLF4J ServiceLoader compatibility on Arclight/hybrid servers
    mergeServiceFiles()
    // Keep size reasonable but preserve classes loaded dynamically via ServiceLoader
    minimize {
        // Jetty WebSocket extensions are loaded dynamically via ServiceLoader
        exclude(dependency("org.eclipse.jetty.websocket:.*:.*"))
        exclude(dependency("org.eclipse.jetty:jetty-util:.*"))
        // SLF4J simple logger for Javalin on hybrid servers
        exclude(dependency("org.slf4j:.*:.*"))
    }
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

// =============================================================================
// GitHub Release Task
// =============================================================================
// Usage:
//   .\gradlew.bat githubRelease -Pnotes="Your release notes here"
//   .\gradlew.bat githubRelease -Pnotes="## Fixed\n- Bug fix 1\n- Bug fix 2"
//   .\gradlew.bat githubRelease -Ptag="v1.5.7" -Ptitle="Custom Title" -Pnotes="Notes"
//   .\gradlew.bat githubRelease -Pdraft=true -Pnotes="Draft release"
//   .\gradlew.bat githubRelease -Pprerelease=true -Pnotes="Beta release"
//
// Properties:
//   -Pnotes      (REQUIRED) Release notes/changelog
//   -Ptag        (optional) Git tag, defaults to "v${version}"
//   -Ptitle      (optional) Release title, defaults to "The Endex ${version}"
//   -Pdraft      (optional) Create as draft release (true/false)
//   -Pprerelease (optional) Mark as pre-release (true/false)
// =============================================================================

tasks.register<Exec>("githubRelease") {
    dependsOn("build")
    group = "publishing"
    description = "Creates a GitHub release with both Paper and Spigot JARs"
    
    doFirst {
        // Validate that notes are provided
        if (!project.hasProperty("notes") || project.property("notes").toString().isBlank()) {
            throw GradleException("""
                |
                |ERROR: Release notes are required!
                |
                |Usage: .\gradlew.bat githubRelease -Pnotes="Your release notes"
                |
                |Examples:
                |  .\gradlew.bat githubRelease -Pnotes="## Fixed\n- Bug fix"
                |  .\gradlew.bat githubRelease -Ptag="v1.5.7" -Pnotes="Release notes"
                |  .\gradlew.bat githubRelease -Pdraft=true -Pnotes="Draft notes"
                |
            """.trimMargin())
        }
    }
    
    // Get properties with defaults
    val releaseTag = if (project.hasProperty("tag")) project.property("tag").toString() else "v${version}"
    val releaseTitle = if (project.hasProperty("title")) project.property("title").toString() else "The Endex ${version}"
    val releaseNotes = if (project.hasProperty("notes")) project.property("notes").toString() else ""
    val isDraft = project.hasProperty("draft") && project.property("draft").toString().toBoolean()
    val isPrerelease = project.hasProperty("prerelease") && project.property("prerelease").toString().toBoolean()
    
    // Build the gh release command
    val paperJar = "release/TheEndex-${version}.jar"
    val spigotJar = "release/TheEndex-${version}-spigot.jar"
    
    // Construct full release notes with download info
    val fullNotes = """
        |${releaseNotes.replace("\\n", "\n")}
        |
        |## Downloads
        |- **TheEndex-${version}.jar** - For Paper servers (lightweight, uses Paper's library loader)
        |- **TheEndex-${version}-spigot.jar** - For Spigot/other servers (all-in-one with bundled dependencies)
    """.trimMargin()
    
    // Build command arguments
    val args = mutableListOf(
        "gh", "release", "create", releaseTag,
        "--title", releaseTitle,
        "--notes", fullNotes,
        paperJar, spigotJar
    )
    
    if (isDraft) args.addAll(listOf("--draft"))
    if (isPrerelease) args.addAll(listOf("--prerelease"))
    
    commandLine(args)
    
    doLast {
        println("")
        println("✅ GitHub Release created successfully!")
        println("   Tag: $releaseTag")
        println("   Title: $releaseTitle")
        println("   URL: https://github.com/khristianaarongarcia/endex/releases/tag/$releaseTag")
        println("")
    }
}

// Quick release task that just builds and copies without GitHub upload
tasks.register("prepareRelease") {
    dependsOn("build")
    group = "publishing"
    description = "Builds both JARs and copies to release folder (no GitHub upload)"
    
    doLast {
        println("")
        println("✅ Release JARs prepared in release/ folder:")
        println("   - TheEndex-${version}.jar (Paper)")
        println("   - TheEndex-${version}-spigot.jar (Spigot)")
        println("")
        println("To upload to GitHub, run:")
        println("   .\\gradlew.bat githubRelease -Pnotes=\"Your release notes\"")
        println("")
    }
}
