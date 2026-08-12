package com.qa.crd.utils;

import com.qa.crd.dtos.Holding;
import com.qa.crd.dtos.PortfolioResponse;
import com.qa.crd.dtos.RebalanceResponse;
import com.qa.crd.dtos.TradeRecommendation;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

public class PortfolioBalancer {

    public static PortfolioResponse getPortfolioResponse(Response  response){
                return response
                .then()
                .extract()
                .as(PortfolioResponse.class);
    }

    public static RebalanceResponse getRebalanceResponse(Response  response){
        return response
                .then()
                .extract()
                .as(RebalanceResponse.class);
    }

    public static List<TradeRecommendation> calculateTrades(PortfolioResponse portfolio) {
        double totalAssets = portfolio.getTotalAssets();
        return portfolio.getHoldings().stream()
                .map(h -> toTrade(h, totalAssets))
                .collect(Collectors.toList());
    }

    private static TradeRecommendation toTrade(Holding h, double totalAssets) {
        // variance = current - target; negative means underweight (BUY), positive means overweight (SELL)
        double variance = h.getCurrentPercent() - h.getTargetPercent();
        if (variance == 0) {
            return new TradeRecommendation(h.getSymbol(), "HOLD", 0);
        }
        int shares = (int) Math.round(Math.abs(variance) / 100.0 * totalAssets / h.getUnitPrice());
        String action = variance < 0 ? "BUY" : "SELL";
        return new TradeRecommendation(h.getSymbol(), action, shares);
    }
}