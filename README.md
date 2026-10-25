# QA Hybrid Automation Framework

![CI](https://github.com/<your-username>/qa-hybrid-framework/actions/workflows/ci.yml/badge.svg)

A single Java framework that tests a **web UI**, a **REST API**, and **database data integrity**,
with both **TestNG** and **Cucumber BDD** layers, **Playwright** alongside **Selenium**,
parallel execution, and CI/CD with published Allure reports.

| Layer | Tooling | Target |
|---|---|---|
| UI | Selenium WebDriver, Page Object Model | [saucedemo.com](https://www.saucedemo.com) |
| UI (modern) | Playwright for Java | saucedemo.com |
| API | RestAssured, JSON-schema validation | [restful-booker](https://restful-booker.herokuapp.com) |
| Database | JDBC + SQL assertions (H2, swappable for MySQL/Oracle) | API data mirrored into a table |
| BDD | Cucumber + PicoContainer | Gherkin features for UI and API |
| Reporting | Allure (screenshots, request/response attachments) | GitHub Pages / pipeline artifact |
| CI/CD | GitHub Actions, Azure DevOps | headless runs on every push |

## Quick start

Prerequisites: JDK 17+, Maven 3.9+, Chrome.

```bash
git clone https://github.com/<your-username>/qa-hybrid-framework.git
cd qa-hybrid-framework
mvn clean test
```

## Common commands

```bash
mvn test -DsuiteFile=testng-smoke.xml          # smoke tests only (tag: smoke)
mvn test -Dheadless=true                       # no browser window
mvn test -Dbrowser=firefox                     # chrome | firefox | edge
mvn test -Dcucumber.filter.tags="@api"         # filter BDD scenarios
mvn allure:serve                               # open the Allure report locally
```

Any value in `config.properties` can be overridden with `-Dkey=value` or an environment
variable (`ui.password` becomes `UI_PASSWORD`). For a real system, pass credentials
through your CI secret store and never commit them.

## Project layout

```
src/test/java/com/qa/framework/
  config/      ConfigReader (system property > env var > config.properties)
  core/        DriverFactory (ThreadLocal), BaseUITest, RetryAnalyzer/Listener
  pages/       Page Objects: Login, Inventory, Cart, Checkout
  api/         ApiClient (shared spec), BookingApi, request models
  db/          BookingRepository (JDBC helper)
  data/        BookingFactory (unique test data per run)
  tests/       ui/  api/  db/  playwright/   (TestNG)
  steps/       Cucumber step definitions, hooks, shared TestContext
  runners/     Cucumber TestNG runner
src/test/resources/
  features/    login, checkout, booking_api (.feature)
  schemas/     JSON schemas for contract tests
testng.xml / testng-smoke.xml    full regression and smoke suites
```

## Design decisions

- **Thread-safe by design:** one WebDriver per thread via `ThreadLocal`; the suite runs classes in parallel.
- **Self-contained tests:** every test creates its own data, so order never matters.
- **Flake handling:** a retry listener reruns a failed test once, so real defects stay visible and one-off network blips don't.
- **Contract + behaviour:** API tests assert both JSON schema and field values.
- **Selenium vs Playwright:** the same checkout flow is implemented in both so the trade-offs are visible
  (explicit waits and page objects vs auto-waiting and web-first assertions).
- **Database layer:** the public demo API has no accessible database, so responses are mirrored into H2
  to demonstrate SQL validation (data match, date-range integrity, aggregates). Point `db.url` at
  MySQL/Oracle to run the same checks against a real schema.

## CI/CD

- `.github/workflows/ci.yml`: runs headless on push/PR, uploads results, and publishes the Allure report
  to GitHub Pages (enable Pages from the `gh-pages` branch in repo settings).
- `azure-pipelines.yml`: same run on Azure DevOps with JUnit results and Allure artifacts.

## Report

_Add a screenshot of your Allure report here once the first run completes._
