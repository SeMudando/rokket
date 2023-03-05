group = "br.com.semudando"
version = "0.1.0"

@Suppress("DSL_SCOPE_VIOLATION") //KTIJ-19369
plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.detekt)
  idea
}

repositories {
  mavenCentral()
}

sourceSets {
  create("integrationTest") {
    compileClasspath += configurations.testCompileClasspath + sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += configurations.testRuntimeClasspath + sourceSets.main.get().output + sourceSets.test.get().output
  }
}

idea {
  module {
    testSources.from(sourceSets["integrationTest"].kotlin.srcDirs)
  }
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

  testImplementation(libs.mongodb.container)
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
