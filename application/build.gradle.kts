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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

application {
    mainClass.set("com.it.exalt.MainKt")
}
