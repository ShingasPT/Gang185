plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.shingaspt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-beta.3")
    implementation("io.github.cdimascio:dotenv-java:2.3.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}