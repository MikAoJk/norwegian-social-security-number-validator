import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "io.github.mikaojk"
version = System.getenv("NEW_VERSION") ?: "1.0.0"

val junitJupiterVersion = "5.12.0"
val kotlinVersion = "2.1.10"
val ktfmtVersion = "0.44"

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("com.diffplug.spotless") version "7.0.2"
    id("com.vanniktech.maven.publish") version "0.30.0"
}


kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}




mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "norwegian-social-security-number-validator", version.toString())

    pom {
        name.set("norwegian-social-security-number-validator")
        description.set("Library for validating a norwegian social security number (FNR or DNR)")
        url.set("https://github.com/MikAoJk/norwegian-social-security-number-validator")
        inceptionYear.set("2024")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://github.com/MikAoJk/norwegian-social-security-number-validator.git")
            }
        }

        developers {
            developer {
                id.set("MikAoJk")
                name.set("Joakim Taule Kartveit")
                email.set("joakimkartveit@gmail.com")
                url.set("https://github.com/MikAoJk/")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/MikAoJk/norwegian-social-security-number-validator.git")
            developerConnection.set("scm:git:https://github.com/MikAoJk/norwegian-social-security-number-validator.git")
            url.set("https://github.com/MikAoJk/norwegian-social-security-number-validator")
        }
        version = System.getenv("NEW_VERSION")
    }

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
