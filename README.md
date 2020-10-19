# Triangles Service REST API Autotests Project

### Description

Testing REST API services for managing triangles and calculating their perimeter and area.


### Project Structure

  * "test-framework" - Framework for making API requests, properties setup, keeping constants and data models requested for test.
  * "autotests" - Autotests.
  
Autotests are separated to test suites. Tests in suites can be run in parallel except "limits" suite as it contains tests that check limits of number of triangles added to the service.
  
##### Basic Dependencies

  * JUnit 5
  * Rest Assured
  * Jackson
  * Allure reports 


### Run Autotests

Run all tests:
```
./gradlew -p autotests clean test
```

Run all tests in suite:
```
./gradlew -p autotests clean test -Dsuite={SUITE_NAME}
```

SUITE_NAME = package name. All suites names can be found in "/autotests/build.gradle" file.

Run all tests in class:
```
./gradlew -p autotests clean test -Dname={CLASS_NAME}
```

Run test method:
```
./gradlew -p autotests clean test -Dname={CLASS_NAME.METHOD_NAME}
```

Run all tests with N parallel threads:
```
./gradlew -p autotests clean test -Dpar=N
```

Run all tests and retry failed up to 2 times:
```
./gradlew -p autotests clean test -Dretry=3
```


### Allure Reports

Build Allure report (Allure should be installed):
```
./gradlew -p autotests allureServe
```


### Found Issues

Found issues are documented in "issues/found_issues.pdf" file.
