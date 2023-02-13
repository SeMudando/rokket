group = "br.com.semudando"
version = "0.1.0"

@Suppress("DSL_SCOPE_VIOLATION") //KTIJ-19369
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
}

repositories {
    mavenCentral()
}

dependencies {
    // Jackson
    implementation(libs.bundles.jackson)

    // Ktor Client
    implementation(libs.bundles.ktor.client)

    // Reflections
    implementation(libs.reflections)

}
