package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the allocation of items to a specific truck.
 * It contains the list of allocations, the truck, and the associated statistics for that truck.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckAllocation {

    /**
     * The truck being loaded in this allocation.
     */
    private Truck truck;

    /**
     * A list of Allocations, each representing an item allocated to the truck.
     */
    private List<Allocation> allocations;

    /**
     * The total volume used by the truck's cargo in this allocation.
     */
    private double totalVolumeUsed;

    /**
     * The total weight used by the truck's cargo in this allocation.
     */
    private double totalWeightUsed;
}
