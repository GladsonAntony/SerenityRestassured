# Sample project - API testing with RestAssured, JUnit & Serenity BDD 
A sample gradle project demonstrating automated API tests using RestAssured, Junit, and Serenity BDD.

## API tests
There are junit tests written with and without Serenity BDD 
1) Reassured Junit tests - CountrySearchTests and GetWeatherTests
2) Reassured Junit tests with Serenity BDD. Serenity step can be found in the steps directory. The actual Serenity test resides in the features directory.

## Build.gradle
Serenity plugin is required in order to run Serenity tests with Gradle.

In addition, these dependencies are required:
- Serenity core
- Serenity JUnit
- Serenity Rest Assured

## Usage 
use `gradle clean test` to run tests.
Gradle's default test reports are in the build/reports directory. In addition, Serenity report which shows only the serenity tests can be found in the target/site/serenity directory. 

## Further information
[RESTful API Testing Using Serenity and REST Assured - A Guide](https://www.blazemeter.com/blog/restful-api-testing-using-serenity-and-rest-assured-a-guide)
[First Test with Rest-Assured](http://toolsqa.com/rest-assured/rest-assured-test/)
