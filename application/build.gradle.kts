plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    
    // Test dependencies for API-level tests (RestAssured used in application tests)
    testImplementation("io.rest-assured:rest-assured:4.5.1")
    // (kotlin extensions removed: using a small test-only DSL instead)
    
    // Production dependencies for the web API (Spring)
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    // Spring Security for authentication/authorization
    implementation("org.springframework.boot:spring-boot-starter-security:3.1.5")
    // JPA + embedded DB for integration tests and persistence adapters
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
    runtimeOnly("com.h2database:h2:2.2.220")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

application {
    mainClass.set("com.it.exalt.MainKt")
}
