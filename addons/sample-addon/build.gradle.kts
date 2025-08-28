plugins {
    kotlin("jvm") version "2.2.20-RC"
}

group = "org.lokixcz.theendex"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Compile against the root project to access latest addon APIs
    compileOnly(project(":"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
