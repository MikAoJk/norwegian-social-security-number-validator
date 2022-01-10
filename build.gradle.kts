import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


group = "io.github.MikAoJk"
version = "1.0.0"

val junitJupiterVersion = "5.8.2"
val kotlinVersion = "1.6.0"
val logbackVersion = "1.2.10"
val logstashEncoderVersion = "7.0.1"
val javaVersion = "11"

plugins {
    kotlin("jvm") version "1.6.0"
    `maven-publish`
    java
    signing
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
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
                
                developers {
                    developer {
                        id.set("MikAoJk")
                        name.set("Joakim Taule Kartveit")
                        email.set("joakimkartveit@gmail.com")
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}


tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }

    withType<Javadoc> {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }
}
