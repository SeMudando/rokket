group = "br.com.semudando"
version = "0.1.0"

@Suppress("DSL_SCOPE_VIOLATION") //KTIJ-19369
plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.detekt)
}

repositories {
  mavenCentral()
}

dependencies {
  // Jackson
  implementation(libs.bundles.jackson)

  // Ktor Client
  implementation(libs.bundles.ktor.client)

  // Classgraph
  implementation(libs.classgraph)

  // Kotest
  testImplementation(libs.bundles.kotest)

  // Mockk
  testImplementation(libs.mockk)

  // Log
  implementation(libs.slf4j.simple)
}

kotlin {
  explicitApi()
}

tasks.withType<Test> {
  useJUnitPlatform()
}

detekt {
  autoCorrect = true
}
