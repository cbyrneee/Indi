plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"

    application
}

group = "dev.cbyrne.indi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}

dependencies {
    // implementation("net.dv8tion:JDA:4.2.1_265")
    implementation("com.github.DV8FromTheWorld:JDA:0feb089")
    implementation("com.sedmelluq:lavaplayer:1.3.77")

    implementation("org.apache.logging.log4j:log4j-api:2.8")
    implementation("org.apache.logging.log4j:log4j-core:2.8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.8")

    implementation("org.litote.kmongo:kmongo:4.2.7")

    implementation("org.apache.commons:commons-lang3:3.12.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("dev.cbyrne.indi.IndiKt")
}
