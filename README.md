[![Build and test](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-and-test.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-and-test.yml)

[![Validate Gradle Wrapper](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml/badge.svg?branch=main)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/gradle-wrapper-validation.yml)

[![Build and publish artifact](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-release.yml/badge.svg)](https://github.com/MikAoJk/norwegian-social-security-number-validator/actions/workflows/build-release.yml)

![GitHub Release](https://img.shields.io/github/v/release/MikAoJk/norwegian-social-security-number-validator)


# norwegian-social-security-number-validator
a simple validator for validation a norwegian social security numbers (FNR, DNR)

See [Fødselsnummer](https://no.wikipedia.org/wiki/F%C3%B8dselsnummer#Oppbygning)
for more info on how validation of a norwegian social security number is done

## Technologies used
* Kotlin
* Gradle
* JDK 21

## Using the library 
### Getting github-package-registry packages
The package  in this repo is uploaded to the GitHub Package Registry which requires authentication. It can, for example, be solved like this in Gradle:
```
val githubUser: String by project
val githubPassword: String by project
repositories {
    maven {
        credentials {
            username = githubUser
            password = githubPassword
        }
        setUrl("https://maven.pkg.github.com/mikaojk/norwegian-social-security-number-validator")
    }
}
```
`githubUser` and `githubPassword` can be put into a separate file `~/.gradle/gradle.properties` with the following content:

```                                                     
githubUser=x-access-token
githubPassword=[token]
```

Replace `[token]` with a personal access token with scope `read:packages`.
See githubs guide [creating-a-personal-access-token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) on
how to create a personal access token.

Alternatively, the variables can be configured via environment variables:

* `ORG_GRADLE_PROJECT_githubUser`
* `ORG_GRADLE_PROJECT_githubPassword`

or the command line:

``` bash
./gradlew -PgithubUser=x-access-token -PgithubPassword=[token]
```

### import
Gradle(kotlin)
```
implementation("io.github.mikaojk:norwegian-social-security-number-validator:2.0.9")
```

Maven
```
<dependency>
  <groupId>io.github.mikaojk</groupId>
  <artifactId>norwegian-social-security-number-validator</artifactId>
  <version>2.0.9</version>
</dependency>
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


#### Publish of artifact
Artifact publish is done by GitHub Actions,
and publish to Maven Central

## Contact

This project is maintained by [CODEOWNERS](CODEOWNERS)

Questions and/or feature requests?
Please create an [issue](https://github.com/MikAoJk/norwegian-social-security-number-validator/issues)

## ✏️ Contributing

To get started, please fork the repo and checkout a new branch.
See more info in [CONTRIBUTING.md](CONTRIBUTING.md)
