import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.github.MikAoJk"
version = "1.0.0" //This will never change. See GitHub releases for releases of artifact

val junitJupiterVersion = "5.9.3"
val kotlinVersion = "1.8.22"
val javaVersion = "17"

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.ben-manes.versions") version "0.47.0"
    `maven-publish`
    java
    signing
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)

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
                description.set("Library for validating a norwegian social security number (FNR or DNR)")
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
                version = System.getenv("NEW_VERSION")
            }
            from(components["java"])
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
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
            events("passed", "skipped", "failed")
        }
    }
}
