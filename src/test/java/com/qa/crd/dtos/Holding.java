package com.qa.crd.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Holding {
    private String symbol;
    private double targetPercent;
    private double currentPercent;
    private double unitPrice;
}