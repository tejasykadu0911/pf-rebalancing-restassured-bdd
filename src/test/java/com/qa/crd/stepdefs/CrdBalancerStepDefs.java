package com.qa.crd.stepdefs;

import com.qa.crd.context.ScenarioContext;
import com.qa.crd.dtos.Holding;
import com.qa.crd.dtos.PortfolioResponse;
import com.qa.crd.dtos.RebalanceResponse;
import com.qa.crd.dtos.TradeRecommendation;
import com.qa.crd.steps.CrdBalancerSteps;
import com.qa.crd.utils.PortfolioBalancer;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.qa.crd.utils.PortfolioBalancer.getPortfolioResponse;
import static org.junit.jupiter.api.Assertions.*;

public class CrdBalancerStepDefs {

    Logger LOG = LoggerFactory.getLogger(CrdBalancerStepDefs.class);

    private final ScenarioContext context;
    private final CrdBalancerSteps crdBalancerSteps;

    public CrdBalancerStepDefs(ScenarioContext context, CrdBalancerSteps crdBalancerSteps) {
        this.context = context;
        this.crdBalancerSteps = crdBalancerSteps;
    }

    @Given("portfolio account {word}")
    public void portfolioAccount(String accountId) {
        context.setAccountId(accountId);
    }

    @When("send request to get account portfolio details")
    public void sendRequestToGetAccountPortfolioDetails() {
        crdBalancerSteps.sendRequestToGetAccountPortfolioDetails();
    }

    @Then("verify account portfolio details has been retrieved")
    public void verifyAccountPortfolioDetailsHasBeenRetrieved() {
        context.getPortfolioResponse()
                .then()
                .statusCode(200);
    }

    @Then("Verify account portfolio is not balanced")
    public void verifyAccountPortfolioIsNotBalanced() {
        PortfolioResponse portfolio = getPortfolioResponse(context.getPortfolioResponse());

        LOG.info("response {}",context.getPortfolioResponse().asString());

        assertFalse(portfolio.isBalanced(),
                "Expected portfolio to be unbalanced but it was balanced");
    }

    @Then("Verify account portfolio is balanced")
    public void verifyAccountPortfolioIsBalanced() {
        PortfolioResponse portfolio = getPortfolioResponse(context.getPortfolioResponse());

        LOG.info("response {}",context.getPortfolioResponse().asString());

        assertTrue(portfolio.isBalanced(),
                "Expected portfolio to be unbalanced but it was balanced");
    }

    @When("post request to get rebalancing recommendations")
    public void postRequestToGetRebalancingRecommendations() {
        crdBalancerSteps.postRebalancingRecommendations();
    }

    @Then("verify the following rebalancing recommendations:")
    public void verifyTheFollowingRebalancingRecommendations(DataTable dataTable) {
        LOG.info("getRecommendationResponse response {}",context.getRecommendationResponse().asString());
        RebalanceResponse rebalanceResponse = PortfolioBalancer.getRebalanceResponse(context.getRecommendationResponse());

        List<TradeRecommendation> actualTrades = rebalanceResponse.getRecommendations();
        List<Map<String, String>> expectedRows = dataTable.asMaps();

        for (Map<String, String> row : expectedRows) {
            String symbol = row.get("security");
            String expectedAction = row.get("action");
            int expectedShares = Integer.parseInt(row.get("shares"));

            TradeRecommendation actual = actualTrades.stream()
                    .filter(t -> t.getSymbol().equals(symbol))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("No trade recommendation found for: " + symbol));

            assertEquals(expectedAction, actual.getAction(),
                    "Action mismatch for " + symbol);
            assertEquals(expectedShares, actual.getShares(),
                    "Shares mismatch for " + symbol);
        }
    }

    @When("post request to perform rebalancing recommendations")
    public void postRequestToPerformRebalancingRecommendations() {
        crdBalancerSteps.postPerformRebalancingRecommendations();
    }

    @Then("Verify portfolio balancing is successful and account portfolio is balanced")
    public void thenVerifyRebalancingIsSuccessful(DataTable dataTable)  {
        LOG.info("perform rebalance response {}", context.getPerformRebalanceResponse().asString());
        PortfolioResponse portfolio = PortfolioBalancer.getPortfolioResponse(context.getPerformRebalanceResponse());

        List<Map<String, String>> expectedRows = dataTable.asMaps();

        for (Map<String, String> row : expectedRows) {
            String symbol = row.get("security");
            double expectedTarget = Double.parseDouble(row.get("target"));

            Holding actual = portfolio.getHoldings().stream()
                    .filter(h -> h.getSymbol().equals(symbol))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("No holding found for: " + symbol));

            assertEquals(expectedTarget, actual.getTargetPercent(),
                    "Target percent mismatch for " + symbol);
            assertEquals(actual.getTargetPercent(), actual.getCurrentPercent(),
                    "Current does not match target for " + symbol + " — variance is not 0");
        }
    }

    @Then("verify account portfolio target sum is 100")
    public void verifyAccountPortfolioTargetSumIs() {
        PortfolioResponse portfolio = getPortfolioResponse(context.getPortfolioResponse());
        double totalTarget = portfolio.getHoldings().stream()
                .mapToDouble(Holding::getTargetPercent)
                .sum();
        assertEquals(100.0, totalTarget, "Total target percent should be 100");
    }

    @And("verify account portfolio current sum is 100")
    public void verifyAccountPortfolioCurrentSumIs() {
        LOG.info("response {}",context.getPortfolioResponse().asString());
        PortfolioResponse portfolio = getPortfolioResponse(context.getPortfolioResponse());
        double totalCurrent = portfolio.getHoldings().stream()
                .mapToDouble(Holding::getCurrentPercent)
                .sum();
        assertEquals(100.0, totalCurrent, "Total target percent should be 100");
    }

    @And("verify share price of each security is greater than 0")
    public void verifySharePriceOfEachSecurityIsGreaterThanZero() {
        PortfolioResponse portfolio = getPortfolioResponse(context.getPortfolioResponse());
        for (Holding holding : portfolio.getHoldings()) {
            assertTrue(holding.getUnitPrice() > 0, "Unit price for " + holding.getSymbol() + " should be greater than 0");
        }
    }
}