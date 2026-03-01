plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
}

application {
    mainClass.set("com.it.exalt.MainKt")
}
