package com.qa.crd.steps;

import com.qa.crd.context.ScenarioContext;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class CrdBalancerSteps {
    private static final Logger logger = LoggerFactory.getLogger(CrdBalancerSteps.class);
    private final ScenarioContext context;

    public CrdBalancerSteps(ScenarioContext context) {
        this.context = context;
    }

    public void sendRequestToGetAccountPortfolioDetails() {
        Response response = given()
                .accept("application/json")
                .when()
                .get("/api/portfolio/" + context.getAccountId());
        context.setPortfolioResponse(response);
        logger.info("Sent request to get account portfolio details for account: {}", context.getAccountId());
    }

    public void postRebalancingRecommendations() {
        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .when()
                .post("/api/recommendations/" + context.getAccountId());
        context.setRecommendationResponse(response);
        logger.info("Posted rebalancing recommendation request for account: {}", context.getAccountId());
    }

    public void postPerformRebalancingRecommendations() {
        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .when()
                .post("/api/rebalance/" + context.getAccountId());
        context.setPerformRebalanceResponse(response);
        logger.info("Posted perform rebalancing request for account: {}", context.getAccountId());
    }

}
