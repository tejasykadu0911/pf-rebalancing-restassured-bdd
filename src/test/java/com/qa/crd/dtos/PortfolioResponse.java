package com.qa.crd.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortfolioResponse {
    private String accountId;
    private boolean balanced;
    private double totalAssets;
    private int vestedPercentage;
    private List<Holding> holdings;
}