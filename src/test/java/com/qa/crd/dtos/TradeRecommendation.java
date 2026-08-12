package com.qa.crd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRecommendation {
    private String symbol;
    private String action;  // BUY / SELL / HOLD
    private int shares;
}