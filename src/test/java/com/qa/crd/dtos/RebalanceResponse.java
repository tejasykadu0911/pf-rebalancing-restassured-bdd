package com.qa.crd.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RebalanceResponse {
    private String accountId;
    private List<TradeRecommendation> recommendations;
}