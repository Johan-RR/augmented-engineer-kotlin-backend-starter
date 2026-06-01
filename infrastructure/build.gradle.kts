plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    implementation(project(":domain"))
    // Spring Data JPA for production adapter + H2 runtime (align Spring Boot version with application)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
    runtimeOnly("com.h2database:h2:2.2.220")

    // Test dependencies for integration tests (Spring test support)
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.5")
}
