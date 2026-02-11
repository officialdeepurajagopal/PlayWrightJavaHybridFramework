# PlayWrightJavaHybridFramework

## Overview
This repository contains a hybrid automation framework built with Playwright for Java. It supports both UI and API testing, integrates with Allure for reporting, and follows best practices for maintainability and scalability.

## Project Structure
```
PlayWrightJavaHybridFramework/
├── pom.xml                  # Maven build file
├── src/
│   ├── main/java/           # Main source code
│   │   └── org/example/
│   │       ├── Main.java
│   │       ├── api/         # API client classes
│   │       ├── model/       # Data models
│   │       ├── ui/          # Page object classes
│   │       └── utils/       # Utility classes
│   ├── main/resources/      # Main resources (config files)
│   ├── test/java/           # Test source code
│   │   └── org/example/tests/
│   │       ├── APITest.java
│   │       ├── BaseTest.java
│   │       └── UITest.java
│   └── test/resources/      # Test resources (testng.xml, configs)
├── allure-results/          # Allure test results
├── target/                  # Maven build output
```

## Features
- UI and API test automation using Playwright for Java
- Page Object Model for UI tests
- API client and model classes for API tests
- Configurable environments (dev, qa)
- Allure reporting integration
- Maven build and dependency management

## Getting Started
1. **Clone the repository:**
   ```bash
   git clone git@github.com:officialdeepurajagopal/PlayWrightJavaHybridFramework.git
   ```
2. **Install dependencies:**
   ```bash
   mvn clean install
   ```
3. **Run tests:**
   ```bash
   mvn test
   ```
4. **View Allure reports:**
   ```bash
   mvn allure:serve
   ```

## Configuration
- Environment-specific properties are located in `src/main/resources` and `src/test/resources`.
- TestNG configuration is in `src/test/resources/testng.xml`.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.

