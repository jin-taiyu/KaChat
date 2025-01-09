val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("io.ktor.plugin") version "2.3.4"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "com.kachat"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-server-core-jvm:2.3.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.4")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.4")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.4")
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.4")

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    implementation("org.mindrot:jbcrypt:0.4")

    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:2.3.4")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.4")
    
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    
    implementation("com.auth0:java-jwt:4.4.0")
    
    implementation("com.zaxxer:HikariCP:5.0.1")
    
    implementation("org.postgresql:postgresql:42.6.0")
    
    implementation("ch.qos.logback:logback-classic:1.4.8")

    implementation("io.micrometer:micrometer-registry-prometheus:1.10.5")
}
