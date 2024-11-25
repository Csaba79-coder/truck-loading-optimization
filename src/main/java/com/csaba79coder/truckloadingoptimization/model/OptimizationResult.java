package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the optimization result for loading multiple trucks.
 * It includes a list of truck allocations and statistics for the entire operation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizationResult {

    /**
     * A list of TruckAllocations, each representing the optimal loading of a specific truck.
     */
    private List<TruckAllocation> truckAllocations;

    /**
     * The total volume used across all trucks in the optimization result.
     */
    private double totalVolumeUsed;

    /**
     * The total weight used across all trucks in the optimization result.
     */
    private double totalWeightUsed;

    /**
     * A flag indicating whether the optimization process was successful.
     */
    private boolean isSuccessful;
}

/*
OptimizationResult: Most már tartalmaz egy List<TruckAllocation> mezőt, amely minden egyes teherautó optimalizált
rakodását tartalmazza. Az isSuccessful flag jelzi, hogy az optimalizálás sikeres volt-e, a totalVolumeUsed és
totalWeightUsed pedig összesítik az összes teherautóra vonatkozó adatokat.

TruckAllocation: Ez a modell reprezentálja egy adott teherautó rakodását. Tartalmazza a teherautót, annak Allocation
listáját, valamint a rakodás összesített térfogatát és súlyát.
*/