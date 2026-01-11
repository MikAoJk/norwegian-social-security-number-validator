import com.diffplug.gradle.spotless.SpotlessTask
import org.gradle.api.tasks.javadoc.Javadoc
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "io.github.mikaojk"
version = System.getenv("NEW_VERSION") ?: "1.0.0"

val junitJupiterVersion = "6.0.1"
val kotlinVersion = "2.3.0"
val ktfmtVersion = "0.44"

plugins {
    kotlin("jvm") version "2.3.0"
    id("com.github.ben-manes.versions") version "0.53.0"
    id("com.diffplug.spotless") version "8.1.0"
    id("com.vanniktech.maven.publish") version "0.35.0"
}


kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}




mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
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
   withType<Exec> {
        println("⚈ ⚈ ⚈ Running Add Pre Commit Git Hook Script on Build ⚈ ⚈ ⚈")
        commandLine("cp", "./.scripts/pre-commit", "./.git/hooks")
        println("✅ Added Pre Commit Git Hook Script.")

    }

    withType<SpotlessTask> {
        spotless{
            kotlin { ktfmt(ktfmtVersion).kotlinlangStyle() }
            check {
                dependsOn("spotlessApply")
               }
             }
         }

    withType<Test> {
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
