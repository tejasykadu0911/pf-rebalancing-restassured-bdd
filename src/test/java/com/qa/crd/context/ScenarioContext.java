package com.qa.crd.context;

import io.restassured.response.Response;

public class ScenarioContext {

    private String accountId;
    private Response portfolioResponse;
    private Response recommendationResponse;
    private Response performRebalanceResponse;

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public Response getPortfolioResponse() { return portfolioResponse; }
    public void setPortfolioResponse(Response portfolioResponse) { this.portfolioResponse = portfolioResponse; }

    public Response getRecommendationResponse() { return recommendationResponse; }
    public void setRecommendationResponse(Response recommendationResponse) { this.recommendationResponse = recommendationResponse; }

    public Response getPerformRebalanceResponse() { return performRebalanceResponse; }
    public void setPerformRebalanceResponse(Response performRebalanceResponse) { this.performRebalanceResponse = performRebalanceResponse; }
}