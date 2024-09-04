import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "io.github.mikaojk"
version = "1.0.0"

val junitJupiterVersion = "5.11.0"
val kotlinVersion = "2.0.20"
val ktfmtVersion = "0.44"
val javaVersion = JavaVersion.VERSION_21

plugins {
    kotlin("jvm") version "2.0.20"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("com.diffplug.spotless") version "6.25.0"
    `maven-publish`
    java
    signing
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}





publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url =
                uri("https://maven.pkg.github.com/MikAoJk/norwegian-social-security-number-validator")
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



signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}


tasks {
    spotless {
        kotlin { ktfmt(ktfmtVersion).kotlinlangStyle() }
        check {
            dependsOn("spotlessApply")
        }
    }


    test {
        useJUnitPlatform()
        testLogging {
            events("skipped", "failed")
            showStackTraces = true
            showExceptions = true
            showCauses = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
