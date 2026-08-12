# CharlesRiver BDD Test Framework

A Behavior-Driven Development (BDD) test automation framework for a portfolio rebalancing API.

## Overview

This framework validates a financial portfolio management API that supports portfolio retrieval, rebalancing recommendations, and trade execution. Tests are written in Gherkin and executed via Cucumber, with WireMock providing mock HTTP endpoints.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Build | Maven 3.x |
| BDD Framework | Cucumber 7.20.1 |
| Test Runner | JUnit Platform Suite 1.11.4 |
| API Testing | REST-Assured 5.5.0 |
| Mocking | WireMock 3.9.1 |
| Reporting | Allure 2.29.0 |
| JSON | Jackson 2.18.2 |
| DI | PicoContainer (Cucumber) |
| Utilities | Lombok 1.18.32, SLF4J 2.0.16 |

## Project Structure

```
src/test/
├── java/com/qa/crd/
│   ├── runners/        # Cucumber JUnit Platform test runner
│   ├── stepdefs/       # Cucumber step definitions (Gherkin bindings)
│   ├── steps/          # REST-Assured API request helpers
│   ├── hooks/          # WireMock lifecycle hooks (before/after)
│   ├── utils/          # Portfolio calculation logic & DTO mappers
│   ├── dtos/           # Request/response models (Lombok POJOs)
│   └── context/        # Scenario-scoped shared state
└── resources/
    ├── features/       # Cucumber .feature files (Gherkin scenarios)
    └── wiremock/
        └── mappings/   # Mock endpoint JSON definitions
```

## API Under Test

The framework tests three endpoints for account `CRD001`:

| Method | Path | Description |
|---|---|---|
| GET | `/api/portfolio/{accountId}` | Retrieve current portfolio holdings |
| POST | `/api/recommendations/{accountId}` | Generate trade recommendations |
| POST | `/api/rebalance/{accountId}` | Execute portfolio rebalancing |

WireMock mocks these endpoints on port `8089`.

## Test Scenarios

Eight BDD scenarios are defined in `src/test/resources/features/crdbalancer.feature`:

1. Verify portfolio is initially unbalanced
2. Generate rebalancing recommendations (BUY 66 IBM, SELL 45 ORCL)
3. Execute rebalancing and verify HTTP 200 response
4. Verify portfolio is balanced after rebalancing (all holdings at 20% target)
5. Verify no further rebalancing is needed after balancing
6. **Regression:** Portfolio allocations sum to 100%
7. **Regression:** All share prices are greater than zero
8. **Regression:** Portfolio has exactly 5 holdings

## Rebalancing Logic

Trades are calculated per holding using:

```
variance      = current_allocation% - target_allocation%
trade_action  = variance < 0 → BUY | variance > 0 → SELL | 0 → HOLD
share_count   = abs(variance) / 100 * total_assets / unit_price
```

Example: IBM at 10% current vs 20% target with $150/share and $1,000,000 total assets → BUY 66 shares.

## Running Tests

**Run all scenarios:**
```bash
mvn test
```

**Generate Allure HTML report:**
```bash
mvn allure:report
```

The report is written to `allure-report/`. Open `allure-report/index.html` in a browser to view results.

**Run a specific tag (example):**
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

## Prerequisites

- Java 17+
- Maven 3.6+

No external services are required — WireMock provides all mock endpoints at runtime.
