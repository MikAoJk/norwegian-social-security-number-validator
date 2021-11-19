import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    `maven-publish`
}

group = "no.taule.kartveit"
version = "1.0.0-SNAPSHOT"

val junitJupiterVersion = "5.8.1"
val kotlinVersion = "1.5.31"
val logbackVersion = "1.2.7"
val logstashEncoderVersion = "7.0"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/MikAoJk/norwegian-social-security-number-validator")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {

            pom {
                name.set("norwegian-social-security-number-validator")
                description.set("Library for validation a norwegian social security number")
                url.set("https://github.com/MikAoJk/norwegian-social-security-number-validator")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/MikAoJk/norwegian-social-security-number-validator.git")
                    developerConnection.set("scm:git:https://github.com/MikAoJk/norwegian-social-security-number-validator.git")
                    url.set("https://github.com/MikAoJk/norwegian-social-security-number-validator")
                }
            }
            from(components["java"])
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}


tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }
}