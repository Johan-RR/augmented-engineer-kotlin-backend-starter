plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use Kotlin test library for testing.
    testImplementation(kotlin("test"))
}

tasks.test {
    // Use JUnit Platform for running tests.
    useJUnitPlatform()
}

kotlin {
    // Apply a specific Kotlin/JVM toolchain to ease working on different environments.
    jvmToolchain(21)
}
