[![Build and test](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-and-test.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-and-test.yml)

[![Validate Gradle Wrapper](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml)

[![Build and publish artifact](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-release.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-release.yml)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.mikaojk/norwegian-social-security-number-validator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.mikaojk/norwegian-social-security-number-validator/)

# norwegian-social-security-number-validator
a simple validator for validation a norwegian social security numbers (FNR, DNR)

See [Fødselsnummer](https://no.wikipedia.org/wiki/F%C3%B8dselsnummer#Oppbygning)
for more info on how validation of a norwegian social security number is done

## Technologies used
* Kotlin
* Gradle
* JDK 21

## Using the library 
### import
Gradle(kotlin)
```
implementation("io.github.mikaojk:norwegian-social-security-number-validator:2.0.1")
```

### Use example
```
val valid = validateSocialSecurityAndDNumber11Digits("01013999900")
```

## Local development started

### Prerequisites
Make sure you have the Java JDK 21 installed
You can check which version you have installed using this command:
``` bash
java -version
 ```

### Running the application locally

#### Build and run tests
To build locally and run the integration tests you can simply run
``` bash
./gradlew clean build
 ```
or on windows
`gradlew.bat clean build`

### Upgrading the gradle wrapper
Find the newest version of gradle here: https://gradle.org/releases/ Then run this command:

``` bash
./gradlew wrapper --gradle-version $gradleVersjon
```

#### Manual publish
To publish you can simply run
``` bash
./gradlew clean build
```
and then run
``` bash
./gradlew publish
```

#### Publish of artifact
Artifact publish is done by GitHub Actions,
and publish to Maven Central and GitHub Packages

## Contact

This project is maintained by [CODEOWNERS](CODEOWNERS)

Questions and/or feature requests?
Please create an [issue](https://github.com/MikAoJk/norwegian-social-security-number-validator/issues)

## ✏️ Contributing

To get started, please fork the repo and checkout a new branch.
See more info in [CONTRIBUTING.md](CONTRIBUTING.md)
