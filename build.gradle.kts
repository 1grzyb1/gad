import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.20"
  application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("com.google.api-client:google-api-client:2.0.0")
  implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
  implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")
  implementation("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClass.set("MainKt")
}