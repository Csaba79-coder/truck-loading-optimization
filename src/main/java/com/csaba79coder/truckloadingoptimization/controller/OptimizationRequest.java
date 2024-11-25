package com.csaba79coder.truckloadingoptimization.controller;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptimizationRequest {

    private List<Item> items;
    private List<Truck> trucks;
    private int[][][] placementMatrix;
}