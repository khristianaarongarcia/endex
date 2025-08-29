plugins {
    kotlin("jvm") version "2.2.20-RC"
}

group = "org.lokixcz.theendex"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(project(":"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

// Auto-copy the built addon jar to the local test server after assemble.
// Adjust targetDir if your server folder differs.
val targetDir = rootProject.layout.projectDirectory.dir("MCTestServer/plugins/TheEndex/addons")

tasks.register<Copy>("copyAddonToServer") {
    description = "Copy crypto addon jar to MCTestServer/plugins/TheEndex/addons"
    group = "distribution"
    dependsOn(tasks.named("jar"))
    from(layout.buildDirectory.dir("libs"))
    include("${project.name}-${project.version}.jar")
    into(targetDir)
}

tasks.named("assemble") {
    finalizedBy("copyAddonToServer")
}
