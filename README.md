# PlayWrightJavaHybridFramework

## Overview
This repository contains a hybrid automation framework built with Playwright for Java. It supports both UI and API testing, integrates with Allure for reporting, and follows best practices for maintainability and scalability.

## Project Structure
```
PlayWrightJavaHybridFramework/
├── pom.xml                          # Maven build file
├── Jenkinsfile                      # CI/CD pipeline definition
├── src/
│   ├── main/java/
│   │   └── org/example/
│   │       ├── ApiService/          # API client classes
│   │       ├── model/               # Data models (e.g. Book)
│   │       ├── ui/                  # Page Object classes
│   │       └── utils/               # Utility classes (ConfigLoader, etc.)
│   ├── test/java/
│   │   └── org/example/tests/
│   │       ├── APITests/
│   │       │   └── APITest.java     # API test cases (group: apitest)
│   │       ├── UITests/
│   │       │   └── UITest.java      # UI test cases  (group: uitest)
│   │       └── Common/
│   │           └── BaseTest.java    # Shared setup / teardown
│   └── test/resources/
│       ├── testng.xml               # TestNG suite configuration
│       ├── config-dev.properties    # Dev environment config
│       └── config-qa.properties     # QA  environment config
└── target/
    ├── allure-results/              # Raw Allure result files (auto-cleaned by mvn clean)
    └── surefire-reports/            # TestNG / Surefire XML reports
```

## Features
- UI automation with **Playwright for Java** (Chromium, Firefox, WebKit)
- API testing with **REST Assured**
- Page Object Model for UI tests
- Data-driven testing with **Jackson** / JSON
- Configurable environments (`dev`, `qa`)
- **Allure** reporting with AspectJ instrumentation
- Maven Surefire for test execution with group-based filtering
- Jenkins CI/CD pipeline

## Prerequisites
| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.8+ |
| Allure CLI *(for local reports)* | 2.x — `brew install allure` |

---

## Getting Started

### 1. Clone the repository
```bash
git clone git@github.com:officialdeepurajagopal/PlayWrightJavaHybridFramework.git
cd PlayWrightJavaHybridFramework
```

### 2. Install dependencies & Playwright browsers
```bash
mvn clean install
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

---

## Running Tests

### Run API Tests only
```bash
mvn clean test -Dgroups=apitest
```

### Run UI Tests only
```bash
mvn clean test -Dgroups=uitest
```

### Run both API and UI Tests
```bash
mvn clean test -Dgroups=uitest,apitest
```

---

## Available Maven Parameters

All parameters can be passed with `-D<param>=<value>` and override the defaults defined in `pom.xml`.

| Parameter  | Default            | Description                                              |
|------------|--------------------|----------------------------------------------------------|
| `groups`   | `uitest,apitest`   | Comma-separated TestNG groups to run                     |
| `env`      | `dev`              | Target environment — loads `config-<env>.properties`     |
| `browser`  | `chromium`         | Browser for UI tests: `chromium`, `firefox`, `webkit`    |
| `headless` | `false`            | Run browser in headless mode: `true` / `false`           |
| `slowMo`   | `1000`             | Milliseconds of slow-motion delay between UI actions     |

### Examples

**API tests against the QA environment:**
```bash
mvn clean test -Dgroups=apitest -Denv=qa
```

**UI tests in headless mode on Firefox:**
```bash
mvn clean test -Dgroups=uitest -Dbrowser=firefox -Dheadless=true -DslowMo=0
```

**UI tests in headless mode on Chromium (CI-friendly):**
```bash
mvn clean test -Dgroups=uitest -Dbrowser=chromium -Dheadless=true -DslowMo=0
```

**Full suite in headless mode:**
```bash
mvn clean test -Dgroups=uitest,apitest -Dheadless=true -DslowMo=0
```

---

## Allure Reports

Allure results are written to `target/allure-results/` during every test run and are automatically cleaned at the start of each build by `mvn clean`.

### Option 1 — Serve report instantly (opens browser automatically)
```bash
allure serve target/allure-results
```

### Option 2 — Generate a static HTML report
```bash
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

### Option 3 — Maven plugin
```bash
mvn allure:report   # generates under target/site/allure-maven-plugin
mvn allure:serve    # generates and opens in browser
```

---

## Configuration

Environment-specific properties are loaded at runtime from `src/test/resources/`:

| File | Used when |
|------|-----------|
| `config-dev.properties` | `-Denv=dev` *(default)* |
| `config-qa.properties`  | `-Denv=qa` |

Each file contains keys such as `appUrl` (for UI tests) and `apiBaseUrl` (for API tests).

---

## CI/CD — Jenkins

The `Jenkinsfile` at the repository root defines the full pipeline. Key parameters exposed in Jenkins:

| Parameter | Default           |
|-----------|-------------------|
| `ENV`     | `dev`             |
| `BROWSER` | `chromium`        |
| `HEADLESS`| `true`            |
| `GROUPS`  | `uitest,apitest`  |

Allure results are collected from `target/allure-results/` and published automatically after every build via the **Allure Jenkins Plugin**.

---

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.
