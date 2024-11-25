package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the allocation of an item to a specific truck.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Allocation { // áru hozzárendelése egy teherautóhoz és pozícióhoz (adatbázis entitásnál ez lesz a kapcsolótábla!)
    private Item item;
    private Truck truck;
    private ItemPositionInTruck itemPositionInTruck;
    /**
     * The axle weights added by this specific allocation.
     * Each index corresponds to a specific axle.
     */
    private double[] axleWeights;
}
