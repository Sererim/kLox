plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}