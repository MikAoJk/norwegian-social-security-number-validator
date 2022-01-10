[![Build and test](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-and-test.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/buildAndTest.yml)

[![Validate Gradle Wrapper](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml)

[![Build and publish artifact](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-release.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/release.yml)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.MikAoJk/norwegian-social-security-number-validator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.MikAoJk/norwegian-social-security-number-validator/)

# norwegian-social-security-number-validator
a simple validator for validation a norwegian social security numbers (FNR, DNR)

See [Fødselsnummer](https://no.wikipedia.org/wiki/F%C3%B8dselsnummer#Oppbygning)
for more info on how validation of a norwegian social security number is done

## Technologies used
* Kotlin
* Gradle
* JDK 11

#### Build and run tests
To build locally and run the integration tests you can simply run `./gradlew clean build` or on windows
`gradlew.bat clean build`

#### Manual publish
To publish you can simply run `./gradlew clean build` and then run `./gradlew publish`

#### Publish of artifact
Artifact publish is done by Github Actions, and publish to Maven Central and Github Packages