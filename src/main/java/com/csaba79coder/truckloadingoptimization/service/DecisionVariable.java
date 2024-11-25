package com.csaba79coder.truckloadingoptimization.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecisionVariable {
    private int[][][] placementMatrix;
    private double[] axleWeights;
}
